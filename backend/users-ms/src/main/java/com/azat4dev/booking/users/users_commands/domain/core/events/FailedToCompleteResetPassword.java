package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;

public record FailedToCompleteResetPassword(
    IdempotentOperationId idempotentOperationId
) implements DomainEventPayload {
}