package com.azat4dev.booking.shared.data.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;


public interface DomainEventSerializer {

    <E extends DomainEventPayload> String serialize(E event);

    <T extends DomainEventPayload> T deserialize(
        Class<T> payloadClass,
        String payload
    );
}
