package com.azat4dev.booking.users.commands.domain.core.values.email.verification;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;

import java.time.LocalDateTime;

public record EmailVerificationTokenInfo(
    UserId userId,
    EmailAddress email,
    LocalDateTime expiresAt
) {
}
