package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;

import java.time.LocalDateTime;

@FunctionalInterface
public interface GetInfoForPasswordResetToken {

    Data execute(TokenForPasswordReset token) throws Exception.InvalidToken;

    record Data(
        UserId userId,
        LocalDateTime expiresAt
    ) {
    }

    // Exceptions

    abstract class Exception extends DomainException {

        public Exception(String message) {
            super(message);
        }

        public static final class InvalidToken extends Exception {
            public InvalidToken() {
                super("Invalid token");
            }
        }
    }
}
