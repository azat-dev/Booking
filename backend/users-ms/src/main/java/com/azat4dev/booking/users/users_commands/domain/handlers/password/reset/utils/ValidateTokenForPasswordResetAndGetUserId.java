package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;

@FunctionalInterface
public interface ValidateTokenForPasswordResetAndGetUserId {

    UserId execute(TokenForPasswordReset token) throws Exception.InvalidToken, Exception.TokenExpired;

    // Exceptions

    abstract sealed class Exception extends DomainException permits Exception.InvalidToken, Exception.TokenExpired {

        Exception(String message) {
            super(message);
        }

        public static final class InvalidToken extends Exception {
            public InvalidToken() {
                super("Invalid token");
            }
        }

        public static final class TokenExpired extends Exception {
            public TokenExpired() {
                super("Token expired");
            }
        }
    }
}
