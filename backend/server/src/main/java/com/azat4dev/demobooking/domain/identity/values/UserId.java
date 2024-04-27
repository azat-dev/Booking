package com.azat4dev.demobooking.domain.identity.values;

import java.util.UUID;

public record UserId(UUID value) {

    public static UserId generateNew() {
        return new UserId(UUID.randomUUID());
    }
}
