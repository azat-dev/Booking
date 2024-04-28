package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.Password;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.users.domain.commands.CreateUser;
import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.users.domain.events.UserCreated;
import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


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

    private EmailAddress anyValidEmail() {
        try {
            return EmailAddress.makeFromString("user@examples.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Password anyValidPassword() {
        try {
            return Password.makeFromString("password111");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private EncodedPassword anyEncodedPassword() {
        return new EncodedPassword("EncodedPassword");
    }

    private Date anyDateTime() {
        return new Date();
    }

    @Test
    void given_valid_user_data__when_createUser__then_add_user_and_produce_event()  {

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
        try {
            sut.usersService.handle(validCommand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
