package com.azat4dev.booking.users.commands.domain.core.events;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;

import java.time.LocalDateTime;

public record UserSignedUp(
    LocalDateTime createdAt,
    UserId userId,
    FullName fullName,
    EmailAddress email,
    EmailVerificationStatus emailVerificationStatus
) implements DomainEventPayload {
}
