package com.azat4dev.booking.shared.infrastructure.bus;

public record TopicListener(
    String topic,
    MessageListener messageListener
) {
}
