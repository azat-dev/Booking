package com.azat4dev.booking.users.commands.application.handlers.password;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.commands.application.commands.password.ResetPasswordByEmail;


public interface ResetPasswordByEmailHandler {

    void handle(ResetPasswordByEmail command) throws Exception.FailedToSendResetPasswordEmail, Exception.EmailNotFound;

    // Exceptions

    abstract class Exception extends DomainException {

        protected Exception(String message) {
            super(message);
        }

        public static final class EmailNotFound extends Exception {
            public EmailNotFound() {
                super("Email not found");
            }
        }

        public static final class FailedToSendResetPasswordEmail extends Exception {
            public FailedToSendResetPasswordEmail() {
                super("Failed to send reset password email");
            }
        }
    }
}
