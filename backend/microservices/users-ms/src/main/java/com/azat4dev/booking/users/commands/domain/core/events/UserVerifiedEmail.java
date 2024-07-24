package com.azat4dev.booking.users.commands.domain.core.events;


import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;

public record UserVerifiedEmail(
    UserId userId,
    EmailAddress emailAddress
) implements DomainEventPayload, EventWithUserId {
}
