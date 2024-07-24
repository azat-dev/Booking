package com.azat4dev.booking.shared.domain.producers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;

@FunctionalInterface
public interface PublishOutboxEvent {

    void execute(
        DomainEventPayload event,
        EventId eventId,
        String tracingInfo
    );
}