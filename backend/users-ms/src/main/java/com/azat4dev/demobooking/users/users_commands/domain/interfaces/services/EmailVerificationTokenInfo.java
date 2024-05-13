package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

import java.time.LocalDateTime;

public record EmailVerificationTokenInfo(
    UserId userId,
    EmailAddress email,
    LocalDateTime expiresAt
) {
}
