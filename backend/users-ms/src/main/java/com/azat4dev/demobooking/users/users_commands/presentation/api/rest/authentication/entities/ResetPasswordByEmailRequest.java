package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.presentation.ValidationException;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record ResetPasswordByEmailRequest(
    String idempotencyKey,
    String email
) {

    public EmailAddress parseEmail() throws ValidationException {
        try {
            return EmailAddress.checkAndMakeFromString(email);
        } catch (DomainException e) {
            throw ValidationException.withPath("email", e);
        }
    }

    public String parseOperationId() throws ValidationException {

        if (idempotencyKey == null || idempotencyKey.isBlank() || idempotencyKey.length() > 100) {
            throw ValidationException.with(
                "NotValid",
                "idempotencyKey",
                "idempotencyKey must not be blank"
            );
        }

        return idempotencyKey;
    }
}
