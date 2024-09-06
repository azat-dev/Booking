package com.azat4dev.booking.shared.infrastructure.bus.kafka;

public record StreamFactoryForTopic(
    String topic,
    StreamFactory factory
) {
}