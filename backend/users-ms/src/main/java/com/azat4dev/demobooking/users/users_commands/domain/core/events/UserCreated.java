package com.azat4dev.demobooking.users.users_commands.domain.core.events;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

import java.time.LocalDateTime;

public record UserCreated(
    LocalDateTime createdAt,
    UserId userId,
    FullName fullName,
    EmailAddress email,
    EmailVerificationStatus emailVerificationStatus
) implements DomainEventPayload {
}
