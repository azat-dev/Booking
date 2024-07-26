package com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public interface OutboxEventSerializer {

    String serialize(DomainEventPayload event);

    DomainEventPayload deserialize(String event, String eventType);
}