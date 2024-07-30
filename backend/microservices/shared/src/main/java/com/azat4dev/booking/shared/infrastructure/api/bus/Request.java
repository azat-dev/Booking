package com.azat4dev.booking.shared.infrastructure.api.bus;

import java.time.LocalDateTime;
import java.util.Optional;

public record Request<T>(
    String id,
    String type,
    LocalDateTime timestamp,
    Optional<String> dynamicReplyAddress,
    T message
) {
}
