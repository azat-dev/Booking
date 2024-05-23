package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;

public record CompletePasswordResetDTO(
    String idempotentOperationId,
    String newPassword,
    String passwordResetToken
) implements DomainEventPayloadDTO {

    public static CompletePasswordResetDTO fromDomain(CompletePasswordReset event) {
        return new CompletePasswordResetDTO(
            event.idempotentOperationId().toString(),
            event.newPassword().value(),
            event.passwordResetToken().getValue()
        );
    }

    public CompletePasswordReset toDomain() {
        return new CompletePasswordReset(
            IdempotentOperationId.dangerouslyMakeFrom(idempotentOperationId),
            new EncodedPassword(newPassword),
            TokenForPasswordReset.dangerouslyMakeFrom(passwordResetToken)
        );
    }
}
