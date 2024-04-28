package com.azat4dev.demobooking.common;

public interface EventsStore {

    void publish(DomainEvent event);
    void close();
}
