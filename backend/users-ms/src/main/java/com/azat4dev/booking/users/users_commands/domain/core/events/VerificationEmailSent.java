package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

public record VerificationEmailSent(
    UserId userId,
    EmailAddress emailAddress
) implements DomainEventPayload {
}
