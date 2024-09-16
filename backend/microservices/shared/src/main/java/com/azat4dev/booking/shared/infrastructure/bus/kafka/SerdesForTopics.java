package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializersForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializersForTopics;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;

@AllArgsConstructor
public final class SerdesForTopics {

    private final MessageSerializersForTopics serializers;
    private final MessageDeserializersForTopics deserializers;

    public Serde<Message> getForTopic(String topic) {
        return new MessageSerde(
            serializers.getForTopic(topic)
                .orElseThrow(() -> new RuntimeException("There is no serializer for topic: " + topic
                    + ". Provide it by using CustomMessageSerializerForTopics bean")),
            deserializers.getForTopic(topic)
                .orElseThrow(() -> new RuntimeException("There is no serializer for topic: " + topic +
                    ". Provide it by using CustomMessageDeserializerForTopics bean"))
        );
    }
}
