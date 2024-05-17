package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.common.presentation.ValidationException;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.ResetPasswordByEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record ResetPasswordByEmailRequest(
    String idempotencyKey,
    String email
) {

    public ResetPasswordByEmail toCommand() {

        try {
            return new ResetPasswordByEmail(
                IdempotentOperationId.checkAndMakeFrom(idempotencyKey),
                EmailAddress.checkAndMakeFromString(email)
            );
        } catch (IdempotentOperationId.Exception e) {
            throw ValidationException.withPath("idempotencyKey", e);
        } catch (EmailAddress.WrongFormatException e) {
            throw ValidationException.withPath("email", e);
        }
    }
}
