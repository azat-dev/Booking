package com.azat4dev.booking.users.users_commands.domain.services;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.core.commands.NewUserData;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.UsersImpl;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


public class UsersImplTests {

    SUT createSUT() {

        UsersRepository usersRepository = mock(UsersRepository.class);

        final var unitOfWork = mock(UnitOfWork.class);
        final var unitOfWorkFactory = mock(UnitOfWorkFactory.class);
        given(unitOfWorkFactory.make()).willReturn(unitOfWork);

        final var outboxEventsRepository = mock(OutboxEventsRepository.class);

        final var markOutboxNeedsSynchronization = mock(UsersImpl.MarkOutboxNeedsSynchronization.class);

        given(unitOfWork.getUsersRepository()).willReturn(usersRepository);
        given(unitOfWork.getOutboxEventsRepository()).willReturn(outboxEventsRepository);

        final var timeProvider = mock(TimeProvider.class);

        return new SUT(
            new UsersImpl(
                timeProvider,
                markOutboxNeedsSynchronization,
                unitOfWorkFactory
            ),
            unitOfWork,
            outboxEventsRepository,
            usersRepository,
            timeProvider,
            markOutboxNeedsSynchronization
        );
    }

    private EncodedPassword anyEncodedPassword() {
        return new EncodedPassword("EncodedPassword");
    }

    private LocalDateTime anyDateTime() {
        return LocalDateTime.now();
    }

    private NewUserData anyNewUserData() {
        final var email = UserHelpers.anyValidEmail();
        final var encodedPassword = anyEncodedPassword();
        final var userId = UserHelpers.anyValidUserId();
        final var fullName = UserHelpers.anyFullName();

        return new NewUserData(
            userId,
            fullName,
            email,
            encodedPassword
        );
    }

    @Test
    void test_createNew_givenValidNewUserData_thenCreateNewAndProduceEvent() {

        // Given
        final var currentTime = anyDateTime();
        final var sut = createSUT();
        final var newUserData = anyNewUserData();

        willDoNothing().given(sut.outboxEventsRepository).publish(any());

        given(sut.timeProvider.currentTime())
            .willReturn(currentTime);

        // When
        try {
            sut.users.createNew(newUserData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Then
        final Consumer<UserCreated> assertUserCreatedEvent = (payload) -> {

            assertThat(payload.userId()).isEqualTo(newUserData.userId());
            assertThat(payload.email()).isEqualTo(newUserData.email());
            assertThat(payload.createdAt()).isEqualTo(currentTime);
            assertThat(payload.emailVerificationStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
            assertThat(payload.fullName()).isEqualTo(newUserData.fullName());
        };

        then(sut.usersRepository)
            .should(times(1))
            .addNew(
                assertArg(userData -> {
                    assertThat(userData.getId()).isEqualTo(newUserData.userId());
                    assertThat(userData.getEmail()).isEqualTo(newUserData.email());
                    assertThat(userData.getEncodedPassword()).isEqualTo(newUserData.encodedPassword());
                    assertThat(userData.getCreatedAt()).isEqualTo(currentTime);
                })
            );

        then(sut.outboxEventsRepository).should(times(1))
            .publish(
                assertArg(assertUserCreatedEvent)
            );

        then(sut.unitOfWork).should(times(1))
            .save();

        then(sut.markOutboxNeedsSynchronization).should(times(1))
            .execute();
    }

    @Test
    void test_createNew_givenValidCommandAndUserExists_thenRollBackAndThrowException() {
        // Given
        final var currentTime = anyDateTime();
        final var sut = createSUT();
        final var newUserData = anyNewUserData();

        willThrow(new UsersRepository.Exception.UserWithSameEmailAlreadyExists())
            .given(sut.usersRepository).addNew(any());

        given(sut.timeProvider.currentTime())
            .willReturn(currentTime);

        // When
        assertThrows(Users.Exception.UserWithSameEmailAlreadyExists.class, () -> {
            sut.users.createNew(newUserData);
        });

        // Then
        then(sut.unitOfWork).should(times(1))
            .rollback();
    }

    @Test
    void test_addVerifiedEmail_givenValidEmail_thanUpdateStatus() throws Exception {

        // Given
        final var sut = createSUT();
        final var user = UserHelpers.anyUser();

        given(sut.usersRepository.findById(user.getId()))
            .willReturn(Optional.of(user));

        // When
        sut.users.addVerifiedEmail(user.getId(), user.getEmail());

        // Then
        then(sut.usersRepository).should(times(1))
            .update(
                assertArg(u -> {
                    assertThat(u.getEmailVerificationStatus()).isEqualTo(EmailVerificationStatus.VERIFIED);
                })
            );

        final var expectedEvent = new UserVerifiedEmail(
            user.getId(),
            user.getEmail()
        );

        then(sut.outboxEventsRepository).should(times(1))
            .publish(expectedEvent);

        then(sut.unitOfWork).should(times(1))
            .save();
    }

    record SUT(
        Users users,
        UnitOfWork unitOfWork,
        OutboxEventsRepository outboxEventsRepository,
        UsersRepository usersRepository,
        TimeProvider timeProvider,
        UsersImpl.MarkOutboxNeedsSynchronization markOutboxNeedsSynchronization
    ) {
    }
}
