package com.azat4dev.demobooking.users.users_commands.domain.handlers;

public interface OutboxEventsPublisher {
    void publishEvents();
}
