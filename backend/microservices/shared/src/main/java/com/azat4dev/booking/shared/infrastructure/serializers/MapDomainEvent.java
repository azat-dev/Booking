package com.azat4dev.booking.shared.infrastructure.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

/**
 * Maps domain event to DTO and back
 * @param <EVENT> Domain event class
 * @param <SERIALIZED> DTO class
 */
public interface MapDomainEvent<EVENT extends DomainEventPayload, SERIALIZED> extends Serializer<EVENT, SERIALIZED> {

    SERIALIZED serialize(EVENT domain);

    EVENT deserialize(SERIALIZED dto);

    Class<EVENT> getOriginalClass();

    Class<SERIALIZED> getSerializedClass();
}
