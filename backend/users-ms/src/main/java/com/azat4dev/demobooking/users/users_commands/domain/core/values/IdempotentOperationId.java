package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import java.util.UUID;

public record IdempotentOperationId (
    UUID value
) {

    public static IdempotentOperationId makeFromString(String value) {
        try {
        return new IdempotentOperationId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new InvalidIdempotentOperationIdException();
        }
    }

    // Exceptions
    public static final class InvalidIdempotentOperationIdException extends RuntimeException {
        public InvalidIdempotentOperationIdException() {
            super("Invalid idempotent operation id. Should be UUID");
        }
    }
}
