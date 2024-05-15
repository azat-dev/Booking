package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.common.presentation.ValidationException;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;

public record CompleteResetPasswordRequest(
    String idempotencyKey,
    String newPassword,
    String resetPasswordToken
) {

    public CompletePasswordReset toCommand(PasswordService passwordService) {

        if (idempotencyKey.isBlank() || idempotencyKey.length() > 100) {
            throw ValidationException.with(
                "InvalidIdempotencyKey",
                "idempotencyKey",
                "idempotencyKey must be between 1 and 100 characters"
            );
        }

        final var password = Password.makeFromString(newPassword);
        final var encodedPassword = passwordService.encodePassword(password);

        return new CompletePasswordReset(
            idempotencyKey,
            encodedPassword,
            TokenForPasswordReset.checkAndMakeFrom(resetPasswordToken)
        );
    }
}
