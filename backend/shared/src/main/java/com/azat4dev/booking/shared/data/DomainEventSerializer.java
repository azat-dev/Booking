package com.azat4dev.booking.shared.data;

import com.azat4dev.booking.shared.domain.event.DomainEvent;


public interface DomainEventSerializer {

    String serialize(DomainEvent<?> event);

    DomainEvent<?> deserialize(String event);
}
