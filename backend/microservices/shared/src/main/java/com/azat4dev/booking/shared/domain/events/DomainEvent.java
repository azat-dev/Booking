package com.azat4dev.booking.shared.domain.events;

import java.time.LocalDateTime;

public record DomainEvent<Payload extends DomainEventPayload>(
    EventId id,
    LocalDateTime issuedAt,
    Payload payload
) {
}
