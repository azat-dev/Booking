package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class CompletePasswordResetHandler implements CommandHandler<CompletePasswordReset> {

    private final ValidatePasswordResetTokenAnGetUserId validatePasswordResetTokenAnGetUserId;
    private final UsersRepository usersRepository;
    private final PasswordService passwordService;
    private final DomainEventsBus bus;

    @Override
    public void handle(CompletePasswordReset command, EventId eventId, LocalDateTime issuedAt) throws InvalidTokenException {

    }

    // Exceptions

    public static final class InvalidTokenException extends DomainException {
        public InvalidTokenException() {
            super("Invalid token");
        }

        @Override
        public String getCode() {
            return "InvalidToken";
        }
    }
}
