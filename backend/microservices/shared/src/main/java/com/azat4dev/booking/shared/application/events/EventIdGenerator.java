package com.azat4dev.booking.shared.application.events;

@FunctionalInterface
public interface EventIdGenerator {
    EventId generate();
}
