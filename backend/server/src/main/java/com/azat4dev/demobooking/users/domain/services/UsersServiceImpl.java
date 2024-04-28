package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.users.domain.commands.CreateUser;
import com.azat4dev.demobooking.users.domain.events.UserCreated;
import com.azat4dev.demobooking.users.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.domain.values.Email;
import com.azat4dev.demobooking.users.domain.values.Password;
import com.azat4dev.demobooking.users.domain.values.WrongEmailFormatException;
import com.azat4dev.demobooking.users.domain.values.WrongPasswordFormatException;
import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.common.utils.TimeProvider;

public final class UsersServiceImpl implements UsersService {

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
    public void handle(CreateUser command) throws WrongEmailFormatException, WrongPasswordFormatException {

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