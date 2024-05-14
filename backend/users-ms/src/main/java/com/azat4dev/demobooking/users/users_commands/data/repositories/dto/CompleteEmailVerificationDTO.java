package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;

public record CompleteEmailVerificationDTO(String token) implements DomainEventPayloadDTO {

    public static CompleteEmailVerificationDTO fromDomain(CompleteEmailVerification domain) {
        return new CompleteEmailVerificationDTO(domain.token().value());
    }

    public CompleteEmailVerification toDomain() {
        return new CompleteEmailVerification(new EmailVerificationToken(token));
    }
}
