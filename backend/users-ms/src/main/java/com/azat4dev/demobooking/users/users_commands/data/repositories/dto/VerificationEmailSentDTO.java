package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

import java.io.Serializable;

public record VerificationEmailSentDTO(
    String userId,
    String email
) implements DomainEventPayloadDTO {

    public static VerificationEmailSentDTO fromDomain(VerificationEmailSent event) {
        return new VerificationEmailSentDTO(
            event.userId().value().toString(),
            event.emailAddress().getValue()
        );
    }

    public VerificationEmailSent toDomain() {
        return new VerificationEmailSent(
            UserId.fromString(userId),
            EmailAddress.dangerMakeWithoutChecks(email)
        );
    }
}