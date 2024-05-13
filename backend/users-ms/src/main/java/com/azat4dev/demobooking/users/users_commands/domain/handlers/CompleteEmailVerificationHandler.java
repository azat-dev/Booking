package com.azat4dev.demobooking.users.users_commands.domain.handlers;


import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationTokensService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CompleteEmailVerificationHandler implements CommandHandler<DomainEventNew<CompleteEmailVerification>> {

    private final EmailVerificationTokensService emailVerificationToken;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;

    @Override
    public void handle(DomainEventNew<CompleteEmailVerification> command) throws TokenIsExpiredException, TokenIsNotValidException {

    }

    // Exceptions

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static final class TokenIsNotValidException extends ValidationException {
        public TokenIsNotValidException() {
            super("Token is not valid");
        }

        @Override
        public String getCode() {
            return "TokenIsNotValid";
        }
    }

    public static final class TokenIsExpiredException extends ValidationException {
        public TokenIsExpiredException() {
            super("Token is expired");
        }

        @Override
        public String getCode() {
            return "TokenIsExpired";
        }
    }
}
