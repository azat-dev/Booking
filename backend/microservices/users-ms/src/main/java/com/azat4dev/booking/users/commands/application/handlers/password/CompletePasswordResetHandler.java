package com.azat4dev.booking.users.commands.application.handlers.password;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.commands.application.commands.password.CompletePasswordReset;

public interface CompletePasswordResetHandler {

    void handle(CompletePasswordReset command) throws Exception, ValidationException;

    // Exceptions

    sealed abstract class Exception extends DomainException permits Exception.InvalidToken, Exception.TokenExpired {
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
                super("Token is expired");
            }
        }
    }

}
