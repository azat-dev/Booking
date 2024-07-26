package com.azat4dev.booking.shared.infrastructure.api.bus;

import java.time.LocalDateTime;

public record InputMessage<T>(
    String id,
    String type,
    LocalDateTime timestamp,
    T message
) {
}
