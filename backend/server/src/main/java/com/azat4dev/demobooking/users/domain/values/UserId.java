package com.azat4dev.demobooking.users.domain.values;

import java.util.UUID;

public record UserId(UUID value) {

    public static UserId generateNew() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId fromString(String id) {
        return new UserId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
