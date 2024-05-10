package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.common.DomainEvent;

public interface OutboxEventsRepository {

    void publish(DomainEvent event);
}