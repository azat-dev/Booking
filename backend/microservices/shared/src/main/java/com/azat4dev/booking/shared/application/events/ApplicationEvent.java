package com.azat4dev.booking.shared.application.events;

import java.time.LocalDateTime;

public record ApplicationEvent<P extends ApplicationEventPayload>(
    EventId id,
    LocalDateTime issuedAt,
    P payload
) {
}
