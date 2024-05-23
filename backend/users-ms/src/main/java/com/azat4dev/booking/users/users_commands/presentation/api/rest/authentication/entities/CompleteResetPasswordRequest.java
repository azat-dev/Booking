package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.booking.common.presentation.ValidationException;
import com.azat4dev.booking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.PasswordService;

public record CompleteResetPasswordRequest(
    String idempotentOperationId,
    String newPassword,
    String resetPasswordToken
) {

    private static ValidationException mapException(Password.Exception e) {
        return ValidationException.withPath("newPassword", e);
    }

    private static ValidationException mapException(TokenForPasswordReset.Exception e) {
        return ValidationException.withPath("resetPasswordToken", e);
    }

    private static ValidationException mapException(IdempotentOperationId.Exception e) {
        return ValidationException.withPath("idempotentOperationId", e);
    }

    public CompletePasswordReset toCommand(PasswordService passwordService) {

        final Password password;
        try {
            password = Password.checkAndMakeFromString(newPassword);
        } catch (Password.Exception e) {
            throw mapException(e);
        }
        final var encodedPassword = passwordService.encodePassword(password);

        try {
            return new CompletePasswordReset(
                IdempotentOperationId.checkAndMakeFrom(idempotentOperationId),
                encodedPassword,
                TokenForPasswordReset.checkAndMakeFrom(resetPasswordToken)
            );
        } catch (TokenForPasswordReset.Exception e) {
            throw mapException(e);
        } catch (IdempotentOperationId.Exception e) {
            throw mapException(e);
        }
    }
}
