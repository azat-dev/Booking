package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

public record FailedToSendVerificationEmailDTO(
    String userId,
    String email,
    int attempts
) implements DomainEventPayloadDTO {

    public static FailedToSendVerificationEmailDTO fromDomain(FailedToSendVerificationEmail event) {
        return new FailedToSendVerificationEmailDTO(
            event.userId().value().toString(),
            event.email().getValue(),
            event.attempts()
        );
    }

    public FailedToSendVerificationEmail toDomain() {
        return new FailedToSendVerificationEmail(
            UserId.dangerouslyMakeFrom(userId),
            EmailAddress.dangerMakeWithoutChecks(email),
            attempts
        );
    }
}
