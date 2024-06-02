package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;

public record FailedToCompleteResetPassword(
    IdempotentOperationId operationId
) implements DomainEventPayload {
}
