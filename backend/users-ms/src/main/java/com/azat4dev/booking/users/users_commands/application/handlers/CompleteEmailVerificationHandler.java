package com.azat4dev.booking.users.users_commands.application.handlers;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.users_commands.application.commands.email.verification.CompleteEmailVerification;

public interface CompleteEmailVerificationHandler {

    void handle(CompleteEmailVerification command) throws Exception;

    // Exceptions

    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class TokenIsNotValid extends Exception {
            public TokenIsNotValid() {
                super("Token is not valid");
            }
        }

        public static final class TokenIsExpired extends Exception {
            public TokenIsExpired() {
                super("Token is expired");
            }
        }

        public static final class EmailNotFound extends Exception {
            public EmailNotFound() {
                super("Email not found");
            }
        }

        public static final class UserNotFound extends Exception {
            public UserNotFound() {
                super("User not found");
            }
        }
    }
}
