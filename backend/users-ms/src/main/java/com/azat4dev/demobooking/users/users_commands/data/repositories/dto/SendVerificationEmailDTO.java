package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

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
            UserId.fromString(userId),
            EmailAddress.dangerMakeWithoutChecks(email),
            fullName.toDomain(),
            attempt
        );
    }
}
