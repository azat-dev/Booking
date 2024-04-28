package com.azat4dev.demobooking.common;

import java.util.Objects;
import java.util.UUID;

public final class CommandId {

    private UUID value;

    private CommandId(UUID value) {
        this.value = value;
    }

    public static CommandId generateNew() {
        return new CommandId(UUID.randomUUID());
    }

    public UUID getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandId commandId)) return false;
        return Objects.equals(getValue(), commandId.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
