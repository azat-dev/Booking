package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import java.util.UUID;

public record IdempotentOperationId (
    UUID value
) {

    public static IdempotentOperationId makeFromString(String value) {
        return new IdempotentOperationId(UUID.fromString(value));
    }
}
