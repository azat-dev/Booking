package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.DomainEventsFactory;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CreateUser;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UsersServiceImpl implements UsersService {

    private final TimeProvider timeProvider;
    private final MarkOutboxNeedsSynchronization markOutboxNeedsSynchronization;
    private final UnitOfWorkFactory unitOfWorkFactory;
    private final DomainEventsFactory domainEventsFactory;

    @Override
    public void handle(CreateUser command) throws UserWithSameEmailAlreadyExistsException {

        final var userId = command.userId();
        final var currentDate = timeProvider.currentTime();

        final var unitOfWork = unitOfWorkFactory.make();

        try {

            final var usersRepository = unitOfWork.getUsersRepository();
            final var outboxEventsRepository = unitOfWork.getOutboxEventsRepository();

            usersRepository.createUser(
                new NewUserData(
                    userId,
                    currentDate,
                    command.email(),
                    command.fullName(),
                    command.encodedPassword(),
                    EmailVerificationStatus.NOT_VERIFIED
                )
            );

            outboxEventsRepository.publish(
                domainEventsFactory.issue(
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

        } catch (UsersRepository.UserWithSameEmailAlreadyExistsException e) {
            unitOfWork.rollback();
            throw new UserWithSameEmailAlreadyExistsException();
        } catch (Throwable e) {
            unitOfWork.rollback();
            throw e;
        }

        markOutboxNeedsSynchronization.execute();
    }

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }
}