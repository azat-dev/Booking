package com.azat4dev.booking.listingsms.commands.domain.values;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(of = "value")
@Getter
public final class OwnerId {

    private final UUID value;

    private OwnerId(UUID value) {
        this.value = value;
    }

    public static OwnerId checkAndMakeFrom(String value) {
        return new OwnerId(UUID.fromString(value));
    }

    public static OwnerId dangerouslyMakeFrom(String value) {
        return new OwnerId(UUID.fromString(value));
    }

    public String toString() {
        return value.toString();
    }
}