package com.azat4dev.demobooking.users.users_commands.domain.entities;

import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.azat4dev.demobooking.users.common.domain.values.UserId;

import java.time.LocalDateTime;

public record User(
    UserId id,
    LocalDateTime createdAt,
    EmailAddress email,
    FullName fullName,
    EncodedPassword encodedPassword,
    EmailVerificationStatus emailVerificationStatus
) {
}
