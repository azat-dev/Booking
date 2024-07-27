package com.azat4dev.booking.shared.infrastructure.api.bus;

import java.time.LocalDateTime;

public record Request<T>(
    String id,
    String type,
    LocalDateTime timestamp,
    T message
) {
}
