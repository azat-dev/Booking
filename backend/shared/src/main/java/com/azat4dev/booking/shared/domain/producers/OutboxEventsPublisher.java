package com.azat4dev.booking.shared.domain.producers;

public interface OutboxEventsPublisher {
    void publishEvents();
}
