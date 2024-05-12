package com.azat4dev.demobooking.users.users_commands.domain.producers;

public interface OutboxEventsPublisher {
    void publishEvents();
}
