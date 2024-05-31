package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.data.DomainEventPayloadDTO;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

public record SendVerificationEmailDTO(
    String userId,
    String email,
    FullNameDTO fullName,
    int attempt
) implements DomainEventPayloadDTO {

    public static SendVerificationEmailDTO fromDomain(SendVerificationEmail event) {
        return new SendVerificationEmailDTO(
            event.userId().value().toString(),
            event.email().getValue(),
            new FullNameDTO(event.fullName()),
            event.attempt()
        );
    }

    public SendVerificationEmail toDomain() {
        return new SendVerificationEmail(
            UserId.dangerouslyMakeFrom(userId),
            EmailAddress.dangerMakeWithoutChecks(email),
            fullName.toDomain(),
            attempt
        );
    }
}
