package com.azat4dev.booking.users.commands.domain.handlers.email.verification;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;

public interface VerifyEmailByToken {

    void execute(EmailVerificationToken token) throws Exception.TokenIsExpired, Exception.TokenIsNotValid, Exception.EmailNotFound, Exception.UserNotFound;

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

        public static final class UserNotFound extends Exception {
            public UserNotFound() {
                super("User not found");
            }
        }

        public static final class EmailNotFound extends Exception {
            public EmailNotFound() {
                super("Email not found");
            }
        }
    }
}
