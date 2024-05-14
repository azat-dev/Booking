package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.SentEmailForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record SentEmailForPasswordResetDTO(
    String userId,
    String email
) implements DomainEventPayloadDTO {

    public static SentEmailForPasswordResetDTO fromDomain(SentEmailForPasswordReset domain) {
        return new SentEmailForPasswordResetDTO(
            domain.userId().value().toString(),
            domain.email().getValue()
        );
    }

    public SentEmailForPasswordReset toDomain() {
        return new SentEmailForPasswordReset(
            UserId.fromString(userId),
            EmailAddress.dangerMakeWithoutChecks(email)
        );
    }
}
