package com.azat4dev.demobooking.users.users_commands.domain.events;

import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.azat4dev.demobooking.users.common.domain.values.UserId;

import java.time.LocalDateTime;

public record UserCreatedPayload(
    LocalDateTime createdAt,
    UserId userId,
    FullName fullName,
    EmailAddress email,
    EmailVerificationStatus emailVerificationStatus
) {
}
