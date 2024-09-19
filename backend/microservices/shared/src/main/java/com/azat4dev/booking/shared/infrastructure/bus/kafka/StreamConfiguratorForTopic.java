package com.azat4dev.booking.shared.infrastructure.bus.kafka;

public record StreamConfiguratorForTopic(
    String channel,
    KafkaStreamConfigurator configurator
) {
}
