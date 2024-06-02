package com.azat4dev.booking.shared.data.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEvent;

public interface SerializableDomainEvent {

    DomainEvent<?> toDomain();
}
