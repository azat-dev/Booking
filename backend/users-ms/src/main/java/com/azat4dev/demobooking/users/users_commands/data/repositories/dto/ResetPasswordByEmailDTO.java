package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.users_commands.domain.core.commands.ResetPasswordByEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record ResetPasswordByEmailDTO(
    String idempotentOperationToken,
    String email
) implements DomainEventPayloadDTO {

    public static ResetPasswordByEmailDTO fromDomain(ResetPasswordByEmail event) {
        return new ResetPasswordByEmailDTO(
            event.idempotentOperationToken().value().toString(),
            event.email().getValue()
        );
    }

    public ResetPasswordByEmail toDomain() {
        return new ResetPasswordByEmail(
            IdempotentOperationId.dangerouslyMakeFrom(idempotentOperationToken),
            EmailAddress.dangerMakeWithoutChecks(email)
        );
    }
}
