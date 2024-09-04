package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializersForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializersForTopics;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;

@AllArgsConstructor
public class SerdesForTopics {

    private final MessageSerializersForTopics serializers;
    private final MessageDeserializersForTopics deserializers;

    public Serde<Message> getForTopic(String topic) {
        return new BusMessageSerde(
            serializers.getForTopic(topic)
                .orElseThrow(() -> new RuntimeException("There is no serializer for topic: " + topic)),
            deserializers.getForTopic(topic)
                .orElseThrow(() -> new RuntimeException("There is no serializer for topic: " + topic))
        );
    }
}
