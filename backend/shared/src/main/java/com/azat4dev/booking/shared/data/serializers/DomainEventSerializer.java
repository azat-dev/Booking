package com.azat4dev.booking.shared.data.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEvent;


public interface DomainEventSerializer {

    String serialize(DomainEvent<?> event);

    DomainEvent<?> deserialize(String event);
}
