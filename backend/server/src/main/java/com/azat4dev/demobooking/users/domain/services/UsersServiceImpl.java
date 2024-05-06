package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.commands.CreateUser;
import com.azat4dev.demobooking.users.domain.events.UserCreated;
import com.azat4dev.demobooking.users.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;

public final class UsersServiceImpl implements UsersService {

    private final TimeProvider timeProvider;
    private final UsersRepository usersRepository;
    private final EventsStore eventsStore;

    public UsersServiceImpl(
        TimeProvider timeProvider,
        UsersRepository usersRepository,
        EventsStore eventsStore
    ) {
        this.timeProvider = timeProvider;
        this.usersRepository = usersRepository;
        this.eventsStore = eventsStore;
    }

    @Override
    public void handle(CreateUser command) throws UserAlreadyExistsException {

        final var userId = command.userId();
        final var currentDate = timeProvider.currentTime();

        try {
            usersRepository.createUser(
                new NewUserData(
                    userId,
                    currentDate,
                    command.email(),
                    command.encodedPassword(),
                    EmailVerificationStatus.NOT_VERIFIED
                )
            );
        } catch (UsersRepository.UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException();
        }

        eventsStore.publish(
            new UserCreated(
                CommandId.generateNew(),
                currentDate.getTime(),
                new UserCreatedPayload(
                    currentDate,
                    userId,
                    command.fullName(),
                    command.email(),
                    EmailVerificationStatus.NOT_VERIFIED
                )
            )
        );
    }
}