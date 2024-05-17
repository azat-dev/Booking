package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;


import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CreateUser;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UsersServiceImpl implements UsersService {

    private final TimeProvider timeProvider;
    private final MarkOutboxNeedsSynchronization markOutboxNeedsSynchronization;
    private final UnitOfWorkFactory unitOfWorkFactory;
    private final EventIdGenerator eventIdGenerator;

    @Override
    public void handle(CreateUser command) throws Exception.UserWithSameEmailAlreadyExists {

        final var userId = command.userId();
        final var currentDate = timeProvider.currentTime();

        final var unitOfWork = unitOfWorkFactory.make();

        try {

            final var usersRepository = unitOfWork.getUsersRepository();
            final var outboxEventsRepository = unitOfWork.getOutboxEventsRepository();

            final var user = User.checkAndMake(
                userId,
                currentDate,
                command.email(),
                command.fullName(),
                command.encodedPassword()
            );

            usersRepository.addNew(user);

            outboxEventsRepository.publish(
                new DomainEventNew<DomainEventPayload>(
                    eventIdGenerator.generate(),
                    currentDate,
                    new UserCreated(
                        currentDate,
                        userId,
                        command.fullName(),
                        command.email(),
                        EmailVerificationStatus.NOT_VERIFIED
                    )
                )
            );

            unitOfWork.save();

        } catch (UsersRepository.Exception.UserWithSameEmailAlreadyExists e) {
            unitOfWork.rollback();
            throw new Exception.UserWithSameEmailAlreadyExists();
        } catch (Throwable e) {
            unitOfWork.rollback();
            throw new RuntimeException(e);
        }

        markOutboxNeedsSynchronization.execute();
    }

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }
}