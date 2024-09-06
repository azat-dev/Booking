package com.azat4dev.booking.shared.infrastructure.bus.kafka;

public record TopicStreamConfigurator(
    String topic,
    KafkaStreamConfigurator configurator
) {
}
