package com.azat4dev.booking.users.commands.domain.core.events;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;

public record FailedToCompleteResetPassword(
    IdempotentOperationId operationId
) implements DomainEventPayload {
}
