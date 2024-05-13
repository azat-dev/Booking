package com.azat4dev.demobooking.common.domain.event;

import java.time.LocalDateTime;

public record DomainEventNew<Payload extends DomainEventPayload>(
    EventId id,
    LocalDateTime issuedAt,
    Payload payload
) {
}
