package com.azat4dev.booking.shared.domain.events;

@FunctionalInterface
public interface EventIdGenerator {
    EventId generate();
}
