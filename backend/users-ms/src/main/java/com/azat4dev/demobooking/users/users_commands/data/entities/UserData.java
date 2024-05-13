package com.azat4dev.demobooking.users.users_commands.data.entities;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;


public record UserData(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String email,
    String encodedPassword,
    String firstName,
    String lastName,
    EmailVerificationStatus emailVerificationStatus
) {
}
