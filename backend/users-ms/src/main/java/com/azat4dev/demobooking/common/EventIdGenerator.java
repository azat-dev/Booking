package com.azat4dev.demobooking.common;

@FunctionalInterface
public interface EventIdGenerator {
    EventId generate();
}
