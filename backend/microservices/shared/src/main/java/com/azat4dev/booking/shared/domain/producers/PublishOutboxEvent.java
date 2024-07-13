package com.azat4dev.booking.shared.domain.producers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;

import java.time.LocalDateTime;

@FunctionalInterface
public interface PublishOutboxEvent {

    void execute(
        DomainEventPayload event,
        LocalDateTime issuedAt,
        EventId eventId,
        String tracingInfo
    );
}