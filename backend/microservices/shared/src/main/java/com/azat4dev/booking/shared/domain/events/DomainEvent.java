package com.azat4dev.booking.shared.domain.events;

import java.time.LocalDateTime;

public record DomainEvent<P extends DomainEventPayload>(
    EventId id,
    LocalDateTime issuedAt,
    P payload
) {

    public <T> T payload(Class<T> clazz) {
        return clazz.cast(payload);
    }
}
