package com.azat4dev.demobooking.users.users_commands.domain.core.events;

import com.azat4dev.demobooking.common.DomainEventPayload;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record FailedToSendVerificationEmail(
    UserId userId,
    EmailAddress email,
    int attempts
) implements DomainEventPayload {
}
