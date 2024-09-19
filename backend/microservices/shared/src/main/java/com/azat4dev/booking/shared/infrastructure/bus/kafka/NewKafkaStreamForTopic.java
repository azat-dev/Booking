package com.azat4dev.booking.shared.infrastructure.bus.kafka;

/**
 * Use this class to create a new Kafka stream for a topic.
 * If you plan to connect it to a message listener with {@link StreamConnectorForTopic}.
 * @param topic
 * @param factory
 */
public record NewKafkaStreamForTopic(
    String topic,
    StreamFactory factory
) {
}
