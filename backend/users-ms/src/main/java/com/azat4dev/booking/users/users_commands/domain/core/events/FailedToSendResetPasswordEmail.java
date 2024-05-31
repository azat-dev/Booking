package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

public record FailedToSendResetPasswordEmail(
    UserId userId,
    EmailAddress email
) implements DomainEventPayload {
}
