package com.azat4dev.demobooking.users.users_commands.domain.events;

import com.azat4dev.demobooking.common.DomainEventPayload;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

public record UserCreatedPayload(
    LocalDateTime createdAt,
    UserId userId,
    FullName fullName,
    EmailAddress email,
    EmailVerificationStatus emailVerificationStatus
) implements DomainEventPayload, Serializable {
    public Object toDTO() {
        return Map.of(
            "createdAt", this.createdAt,
            "userId", this.userId.value(),
            "fullName", Map.of(
                "firstName", this.fullName.firstName().getValue(),
                "lastName", this.fullName.lastName().getValue()
            ),
            "email", this.email.getValue(),
            "emailVerificationStatus", this.emailVerificationStatus.name()
        );
    }
}
