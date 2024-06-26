package com.azat4dev.booking.users.commands.domain.handlers.password.reset;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;

public interface SendResetPasswordEmail {

    void execute(IdempotentOperationId operationId, EmailAddress email) throws Exception;

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
