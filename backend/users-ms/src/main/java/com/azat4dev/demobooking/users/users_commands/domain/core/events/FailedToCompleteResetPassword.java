package com.azat4dev.demobooking.users.users_commands.domain.core.events;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;

public record FailedToCompleteResetPassword(
    String idempotentOperationId
) implements DomainEventPayload {
}
