package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

public record VerificationEmailSent(
    UserId userId,
    EmailAddress emailAddress
) implements DomainEventPayload {
}
