package com.azat4dev.booking.shared.domain.event;

@FunctionalInterface
public interface EventIdGenerator {
    EventId generate();
}
