package com.azat4dev.demobooking.users.users_commands.domain.core.events;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;

public record FailedToCompleteResetPassword(
    IdempotentOperationId idempotentOperationId
) implements DomainEventPayload {
}
