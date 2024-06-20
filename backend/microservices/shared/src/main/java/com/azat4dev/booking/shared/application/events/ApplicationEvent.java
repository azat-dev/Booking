package com.azat4dev.booking.shared.application.events;

import java.time.LocalDateTime;

public record ApplicationEvent<Payload extends ApplicationEventPayload>(
    EventId id,
    LocalDateTime issuedAt,
    Payload payload
) {
}
