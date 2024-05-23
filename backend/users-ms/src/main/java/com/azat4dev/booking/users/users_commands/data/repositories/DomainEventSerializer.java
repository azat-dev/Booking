package com.azat4dev.booking.users.users_commands.data.repositories;

import com.azat4dev.booking.shared.domain.event.DomainEvent;


public interface DomainEventSerializer {

    String serialize(DomainEvent<?> event);

    DomainEvent<?> deserialize(String event);
}
