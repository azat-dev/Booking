package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.kafka.KafkaMessageBus;

public record CustomTopologyForTopic(
    String topic,
    KafkaMessageBus.MakeTopologyForTopic makeTopologyForTopic
) {
}
