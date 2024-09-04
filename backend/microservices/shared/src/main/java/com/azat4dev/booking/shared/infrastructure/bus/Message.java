package com.azat4dev.booking.shared.infrastructure.bus;

import java.time.LocalDateTime;
import java.util.Optional;

public record Message(
    String id,
    String type,
    LocalDateTime sentAt,
    Optional<String> correlationId,
    Optional<String> replyTo,
    Object payload
) {

    public <T> T payloadAs(Class<T> type) {
        return type.cast(payload);
    }
}
