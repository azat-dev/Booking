package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

import java.time.LocalDateTime;

public record UserCreatedDTO(
    LocalDateTime createdAt,
    String userId,
    FullNameDTO fullName,
    String email,
    String emailVerificationStatus
) implements DomainEventPayloadDTO {

    public static UserCreatedDTO fromDomain(UserCreated event) {
        return new UserCreatedDTO(
            event.createdAt(),
            event.userId().value().toString(),
            new FullNameDTO(event.fullName()),
            event.email().getValue(),
            event.emailVerificationStatus().name()
        );
    }

    public UserCreated toDomain() {
        return new UserCreated(
            createdAt,
            UserId.dangerouslyMakeFrom(userId),
            fullName.toDomain(),
            EmailAddress.dangerMakeWithoutChecks(email),
            EmailVerificationStatus.valueOf(emailVerificationStatus)
        );
    }
}
