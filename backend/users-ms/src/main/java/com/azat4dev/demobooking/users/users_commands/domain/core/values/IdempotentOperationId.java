package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import java.util.UUID;

public record IdempotentOperationId (
    UUID value
) {
}
