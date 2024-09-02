package com.azat4dev.booking.shared.infrastructure.bus;

public record CustomTopologyForTopic(
    String topic,
    KafkaMessageBus.MakeTopologyForTopic makeTopologyForTopic
) {
}
