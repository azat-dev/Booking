package com.azat4dev.demobooking.interfaces;

import com.azat4dev.demobooking.domain.common.DomainEvent;

public interface EventsStore {

    void publish(DomainEvent event);
    void close();
}
