package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.data.DomainEventPayloadDTO;
import com.azat4dev.booking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;

public record CompleteEmailVerificationDTO(String token) implements DomainEventPayloadDTO {

    public static CompleteEmailVerificationDTO fromDomain(CompleteEmailVerification domain) {
        return new CompleteEmailVerificationDTO(domain.token().value());
    }

    public CompleteEmailVerification toDomain() {
        return new CompleteEmailVerification(new EmailVerificationToken(token));
    }
}
