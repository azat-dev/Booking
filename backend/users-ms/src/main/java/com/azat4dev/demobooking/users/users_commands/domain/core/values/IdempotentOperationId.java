package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import com.azat4dev.demobooking.common.domain.DomainException;

public record IdempotentOperationId(
    String value
) {

    public static IdempotentOperationId checkAndMakeFrom(String value) throws Exception {
        try {
            return new IdempotentOperationId(value);
        } catch (IllegalArgumentException e) {
            throw new Exception.InvalidIdempotentOperationId();
        }
    }

    public static IdempotentOperationId dangerouslyMakeFrom(String value) {
        return new IdempotentOperationId(value);
    }

    @Override
    public String toString() {
        return value;
    }

    // Exceptions

    public static sealed class Exception extends DomainException permits Exception.InvalidIdempotentOperationId {
        public Exception(String message) {
            super(message);
        }

        public static final class InvalidIdempotentOperationId extends Exception {
            public InvalidIdempotentOperationId() {
                super("Invalid idempotent operation id. Should be UUID");
            }
        }
    }
}
