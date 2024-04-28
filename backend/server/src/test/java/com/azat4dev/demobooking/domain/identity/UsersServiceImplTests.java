package com.azat4dev.demobooking.domain.identity;

import com.azat4dev.demobooking.domain.common.CommandId;
import com.azat4dev.demobooking.domain.identity.commands.CreateUser;
import com.azat4dev.demobooking.domain.common.DomainEvent;
import com.azat4dev.demobooking.domain.identity.events.UserCreated;
import com.azat4dev.demobooking.domain.identity.events.UserCreatedPayload;
import com.azat4dev.demobooking.domain.identity.values.Email;
import com.azat4dev.demobooking.domain.identity.values.Password;
import com.azat4dev.demobooking.domain.identity.values.UserId;
import com.azat4dev.demobooking.interfaces.EventsStore;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.Date;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


interface UsersRepository {

    void createUser(NewUserData newUserData);
}

final class EncodedPassword {

    private final String value;

    public EncodedPassword(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EncodedPassword other)) {
            return false;
        }

        return this.value.equals(other.getValue());
    }
}

final class NewUserData {
    private UserId userId;
    private Date createdAt;
    private Email email;
    private EncodedPassword encodedPassword;

    public NewUserData(
        UserId userId,
        Date createdAt,
        Email email,
        EncodedPassword encodedPassword
    ) {

        if (userId == null) {
            throw new NullPointerException("userId");
        }

        if (createdAt == null) {
            throw new NullPointerException("createdAt");
        }

        if (email == null) {
            throw new NullPointerException("email");
        }

        if (encodedPassword == null) {
            throw new NullPointerException("encodedPassword");
        }

        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.encodedPassword = encodedPassword;
    }

    public UserId getUserId() {
        return userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Email getEmail() {
        return email;
    }

    public EncodedPassword getEncodedPassword() {
        return encodedPassword;
    }
}

interface PasswordService {

    EncodedPassword encodePassword(Password password);

}

class UserCreationFailedWrongPassword extends DomainEvent {

    @Override
    public String getType() {
        return "UserCreationFailedWrongPassword";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public Object getPayload() {
        return null;
    }
}


interface TimeProvider {
    Date currentTime();
}

class UsersServiceImpl implements UsersService {

    private final TimeProvider timeProvider;
    private final UsersRepository usersRepository;
    private final EventsStore eventsStore;
    private final PasswordService passwordService;

    public UsersServiceImpl(
            TimeProvider timeProvider,
            UsersRepository usersRepository,
            PasswordService passwordService,
            EventsStore eventsStore
    ) {
        this.timeProvider = timeProvider;
        this.usersRepository = usersRepository;
        this.passwordService = passwordService;
        this.eventsStore = eventsStore;
    }

    @Override
    public void handle(CreateUser command) {

        final var userId = command.getUserId();
        final var email = Email.makeFromString(command.getEmail());
        final var currentDate = timeProvider.currentTime();

        final var encodedPassword = passwordService.encodePassword(
            Password.makeFromString(command.getPassword())
        );

        usersRepository.createUser(
            new NewUserData(
                userId,
                currentDate,
                email,
                encodedPassword
            )
        );

        eventsStore.publish(
                new UserCreated(
                    CommandId.generateNew(),
                    currentDate.getTime(),
                    new UserCreatedPayload(
                            new User(
                                userId,
                                email,
                                EmailVerificationStatus.NOT_VERIFIED,
                                currentDate
                            )
                    )
                )
        );
    }
}

public class UsersServiceImplTests {

    record SUT(
            UsersService usersService,
            UsersRepository usersRepository,
            PasswordService passwordService,
            EventsStore eventsStore,
            TimeProvider timeProvider
    ) {
    }

    SUT createSUT() {

        UsersRepository usersRepository = mock(UsersRepository.class);
        EventsStore eventsStore = mock(EventsStore.class);

        final var passwordService = mock(PasswordService.class);
        final var timeProvider = mock(TimeProvider.class);

        return new SUT(
                new UsersServiceImpl(
                        timeProvider,
                        usersRepository,
                        passwordService,
                        eventsStore
                ),
                usersRepository,
                passwordService,
                eventsStore,
                timeProvider
        );
    }

    private Email anyValidEmail() {
        return Email.makeFromString("user@examples.com");
    }

    private Password anyValidPassword() {
        return Password.makeFromString("password111");
    }

    private EncodedPassword anyEncodedPassword() {
        return new EncodedPassword("EncodedPassword");
    }

    private Date anyDateTime() {
        return new Date();
    }

    @Test
    void given_valid_user_data__when_RegisterUser__then_add_user_and_produce_event() {

        // Given
        final var sut = createSUT();
        final var email = anyValidEmail();
        final var password = anyValidPassword();
        final var currentTime = anyDateTime();
        final var commandId = CommandId.generateNew();
        final var userId = UserId.generateNew();

        final var validCommand = new CreateUser(
            commandId,
            userId,
            email.getValue(),
            password.getValue()
        );

        final var encodedPassword = anyEncodedPassword();

        willDoNothing().given(sut.eventsStore).publish(any());

        given(sut.timeProvider.currentTime())
            .willReturn(currentTime);

        given(sut.passwordService.encodePassword(any()))
            .willReturn(encodedPassword);

        // When
        sut.usersService.handle(validCommand);

        // Then
        final Consumer<DomainEvent> assertUserCreatedEvent  = (event) -> {
            assertThat(event).isInstanceOf(UserCreated.class);
            final var userCreated = (UserCreated) event;
            final var payload = userCreated.getPayload();
            final var createdUser = payload.getUser();

            assertThat(createdUser.getId()).isEqualTo(userId);
            assertThat(createdUser.getEmail()).isEqualTo(email);
            assertThat(createdUser.getCreatedAt()).isEqualTo(currentTime);
            assertThat(createdUser.getEmailVerificationStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
        };

        then(sut.usersRepository)
            .should(times(1))
            .createUser(
                assertArg(userData -> {
                    assertThat(userData.getUserId()).isEqualTo(userId);
                    assertThat(userData.getEmail()).isEqualTo(email);
                    assertThat(userData.getEncodedPassword()).isEqualTo(encodedPassword);
                    assertThat(userData.getCreatedAt()).isEqualTo(currentTime);
                })
            );

        then(sut.passwordService)
            .should(times(1))
            .encodePassword(password);

        then(sut.eventsStore).should(times(1))
            .publish(
                assertArg(assertUserCreatedEvent)
            );
    }
}
