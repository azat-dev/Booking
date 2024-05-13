package com.azat4dev.demobooking.common.domain.event;

@FunctionalInterface
public interface EventIdGenerator {
    EventId generate();
}
