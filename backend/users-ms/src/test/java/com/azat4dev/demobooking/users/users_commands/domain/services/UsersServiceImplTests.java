package com.azat4dev.demobooking.users.users_commands.domain.services;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CreateUser;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.UsersService;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.UsersServiceImpl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


public class UsersServiceImplTests {

    SUT createSUT() {

        UsersRepository usersRepository = mock(UsersRepository.class);

        final var unitOfWork = mock(UnitOfWork.class);
        final var unitOfWorkFactory = mock(UnitOfWorkFactory.class);
        given(unitOfWorkFactory.make()).willReturn(unitOfWork);

        final var outboxEventsRepository = mock(OutboxEventsRepository.class);
        final var eventIdGenerator = mock(EventIdGenerator.class);

        final var markOutboxNeedsSynchronization = mock(UsersServiceImpl.MarkOutboxNeedsSynchronization.class);

        given(unitOfWork.getUsersRepository()).willReturn(usersRepository);
        given(unitOfWork.getOutboxEventsRepository()).willReturn(outboxEventsRepository);

        final var timeProvider = mock(TimeProvider.class);

        return new SUT(
            new UsersServiceImpl(
                timeProvider,
                markOutboxNeedsSynchronization,
                unitOfWorkFactory,
                eventIdGenerator
            ),
            unitOfWork,
            outboxEventsRepository,
            usersRepository,
            timeProvider,
            eventIdGenerator,
            markOutboxNeedsSynchronization
        );
    }

    private EncodedPassword anyEncodedPassword() {
        return new EncodedPassword("EncodedPassword");
    }

    private LocalDateTime anyDateTime() {
        return LocalDateTime.now();
    }

    private CreateUser anyCreateUserCommand() {
        final var email = UserHelpers.anyValidEmail();
        final var encodedPassword = anyEncodedPassword();
        final var userId = UserHelpers.anyValidUserId();
        final var fullName = UserHelpers.anyFullName();

        return new CreateUser(
            userId,
            fullName,
            email,
            encodedPassword
        );
    }

    @Test
    void test_handle_givenValidCommand_thenCreateUserAndProduceEvent() throws UsersRepository.UserWithSameEmailAlreadyExistsException {

        // Given
        final var currentTime = anyDateTime();
        final var sut = createSUT();
        final var validCommand = anyCreateUserCommand();
        final var expectedEventId = EventId.dangerouslyCreateFrom("expectedEventId");

        willDoNothing().given(sut.outboxEventsRepository).publish(any());

        given(sut.timeProvider.currentTime())
            .willReturn(currentTime);

        given(sut.eventIdGenerator.generate())
            .willReturn(expectedEventId);

        // When
        try {
            sut.usersService.handle(validCommand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Then
        final Consumer<DomainEventNew<UserCreated>> assertUserCreatedEvent = (event) -> {

            final var payload = event.payload();

            assertThat(payload.userId()).isEqualTo(validCommand.userId());
            assertThat(payload.email()).isEqualTo(validCommand.email());
            assertThat(payload.createdAt()).isEqualTo(currentTime);
            assertThat(payload.emailVerificationStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
            assertThat(payload.fullName()).isEqualTo(validCommand.fullName());
        };

        then(sut.usersRepository)
            .should(times(1))
            .addNew(
                assertArg(userData -> {
                    assertThat(userData.getId()).isEqualTo(validCommand.userId());
                    assertThat(userData.getEmail()).isEqualTo(validCommand.email());
                    assertThat(userData.getEncodedPassword()).isEqualTo(validCommand.encodedPassword());
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
    void test_handle_givenValidCommandAndUserExists_thenRollBackAndThrowException() {
        // Given
        final var currentTime = anyDateTime();
        final var sut = createSUT();
        final var validCommand = anyCreateUserCommand();

        willThrow(new UsersRepository.UserWithSameEmailAlreadyExistsException())
            .given(sut.usersRepository).addNew(any());

        given(sut.timeProvider.currentTime())
            .willReturn(currentTime);

        // When
        assertThrows(UsersService.UserWithSameEmailAlreadyExistsException.class, () -> {
            sut.usersService.handle(validCommand);
        });

        // Then
        then(sut.unitOfWork).should(times(1))
            .rollback();
    }

    record SUT(
        UsersService usersService,
        UnitOfWork unitOfWork,
        OutboxEventsRepository outboxEventsRepository,
        UsersRepository usersRepository,
        TimeProvider timeProvider,
        EventIdGenerator eventIdGenerator,
        UsersServiceImpl.MarkOutboxNeedsSynchronization markOutboxNeedsSynchronization
    ) {
    }
}
