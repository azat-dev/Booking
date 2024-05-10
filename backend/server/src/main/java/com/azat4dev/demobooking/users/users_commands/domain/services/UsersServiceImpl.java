package com.azat4dev.demobooking.users.users_commands.domain.services;

import com.azat4dev.demobooking.common.EventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.commands.CreateUser;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;

@RequiredArgsConstructor
public final class UsersServiceImpl implements UsersService {

    @FunctionalInterface
    public interface MarkOutboxNeedsSynchronization {
        void execute();
    }

    private final TimeProvider timeProvider;
    private final UnitOfWork unitOfWork;
    private final EventIdGenerator eventIdGenerator;
    private final MarkOutboxNeedsSynchronization markOutboxNeedsSynchronization;

    @Override
    public void handle(CreateUser command) throws UserWithSameEmailAlreadyExistsException {

        final var userId = command.userId();
        final var currentDate = timeProvider.currentTime();
        final var usersRepository = unitOfWork.getUsersRepository();
        final var outboxEventsRepository = unitOfWork.getOutboxEventsRepository();

        try {
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
                new UserCreated(
                    eventIdGenerator.generate(),
                    currentDate.toInstant(ZoneOffset.UTC).toEpochMilli(),
                    new UserCreatedPayload(
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
        } catch (Exception e) {
            unitOfWork.rollback();
            throw e;
        }

        markOutboxNeedsSynchronization.execute();
    }
}