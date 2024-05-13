package com.azat4dev.demobooking.common.domain.event;

import java.util.UUID;

public final class RandomEventIdGenerator implements EventIdGenerator {

    @Override
    public EventId generate() {
        return EventId.dangerouslyCreateFrom(UUID.randomUUID().toString());
    }
}
