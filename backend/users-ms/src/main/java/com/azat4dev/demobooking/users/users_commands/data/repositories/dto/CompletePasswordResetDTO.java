package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;

public record CompletePasswordResetDTO(
    String idempotentOperationToken,
    String newPassword,
    String passwordResetToken
) implements DomainEventPayloadDTO {

    public static CompletePasswordResetDTO fromDomain(CompletePasswordReset event) {
        return new CompletePasswordResetDTO(
            event.idempotentOperationToken(),
            event.newPassword().getValue(),
            event.passwordResetToken().getValue()
        );
    }

    public CompletePasswordReset toDomain() {
        return new CompletePasswordReset(
            idempotentOperationToken,
            Password.makeFromString(newPassword),
            TokenForPasswordReset.dangerouslyMakeFrom(passwordResetToken)
        );
    }
}
