package com.azat4dev.booking.listingsms.application.config.data.serializer;

import com.azat4dev.booking.shared.data.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.event.DomainEvent;

public final class DomainEventSerializerImpl implements DomainEventSerializer {
    @Override
    public String serialize(DomainEvent<?> event) {
        return "";
    }

    @Override
    public DomainEvent<?> deserialize(String event) {
        return null;
    }
}
