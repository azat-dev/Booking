package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.commands.CreateUser;
import com.azat4dev.demobooking.users.domain.events.UserCreated;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.function.Consumer;

import static com.azat4dev.demobooking.users.domain.UserHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


public class UsersServiceImplTests {

    SUT createSUT() {

        UsersRepository usersRepository = mock(UsersRepository.class);
        EventsStore eventsStore = mock(EventsStore.class);

        final var timeProvider = mock(TimeProvider.class);

        return new SUT(
            new UsersServiceImpl(
                timeProvider,
                usersRepository,
                eventsStore
            ),
            usersRepository,
            eventsStore,
            timeProvider
        );
    }

    private EncodedPassword anyEncodedPassword() {
        return new EncodedPassword("EncodedPassword");
    }

    private Date anyDateTime() {
        return new Date();
    }

    @Test
    void given_valid_user_data__when_createUser__then_add_user_and_produce_event() throws UsersRepository.UserWithSameEmailAndIdAlreadyExistsException {

        // Given
        final var sut = createSUT();
        final var email = anyValidEmail();
        final var encodedPassword = anyEncodedPassword();
        final var currentTime = anyDateTime();
        final var commandId = CommandId.generateNew();
        final var userId = anyValidUserId();
        final var fullName = anyFullName();

        final var validCommand = new CreateUser(
            commandId,
            userId,
            fullName,
            email,
            encodedPassword
        );

        willDoNothing().given(sut.eventsStore).publish(any());

        given(sut.timeProvider.currentTime())
            .willReturn(currentTime);

        // When
        try {
            sut.usersService.handle(validCommand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Then
        final Consumer<DomainEvent> assertUserCreatedEvent = (event) -> {
            assertThat(event).isInstanceOf(UserCreated.class);
            final var userCreated = (UserCreated) event;
            final var payload = userCreated.getPayload();

            assertThat(payload.userId()).isEqualTo(userId);
            assertThat(payload.email()).isEqualTo(email);
            assertThat(payload.createdAt()).isEqualTo(currentTime);
            assertThat(payload.emailVerificationStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
            assertThat(payload.fullName()).isEqualTo(fullName);
        };

        then(sut.usersRepository)
            .should(times(1))
            .createUser(
                assertArg(userData -> {
                    assertThat(userData.userId()).isEqualTo(userId);
                    assertThat(userData.email()).isEqualTo(email);
                    assertThat(userData.encodedPassword()).isEqualTo(encodedPassword);
                    assertThat(userData.createdAt()).isEqualTo(currentTime);
                })
            );

        then(sut.eventsStore).should(times(1))
            .publish(
                assertArg(assertUserCreatedEvent)
            );
    }

    record SUT(
        UsersService usersService,
        UsersRepository usersRepository,
        EventsStore eventsStore,
        TimeProvider timeProvider
    ) {
    }
}
