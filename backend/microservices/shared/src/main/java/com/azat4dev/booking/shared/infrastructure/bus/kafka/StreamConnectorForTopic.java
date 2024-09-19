package com.azat4dev.booking.shared.infrastructure.bus.kafka;

public record StreamConnectorForTopic(
    String topic,
    KafkaStreamConnector configurator
) {
}
