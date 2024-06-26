package com.azat4dev.booking.shared.domain.events;

import java.util.UUID;

public final class RandomEventIdGenerator implements EventIdGenerator {

    @Override
    public EventId generate() {
        return EventId.dangerouslyCreateFrom(UUID.randomUUID().toString());
    }
}
