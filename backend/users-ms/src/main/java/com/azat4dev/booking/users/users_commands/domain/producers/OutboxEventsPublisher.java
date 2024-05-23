package com.azat4dev.booking.users.users_commands.domain.producers;

public interface OutboxEventsPublisher {
    void publishEvents();
}
