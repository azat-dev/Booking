package com.azat4dev.booking.users.users_commands.domain.core.values.email.verification;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

import java.time.LocalDateTime;

public record EmailVerificationTokenInfo(
    UserId userId,
    EmailAddress email,
    LocalDateTime expiresAt
) {
}
