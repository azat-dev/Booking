package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.DeserializersForChannels;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.SerializersForChannels;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;

@AllArgsConstructor
public final class SerdesForTopics {

    private final SerializersForChannels serializers;
    private final DeserializersForChannels deserializers;

    public Serde<Message> getForTopic(String topic) {
        return new MessageSerde(
            serializers.getForChannel(topic)
                .orElseThrow(() -> new RuntimeException("There is no serializer for topic: " + topic
                    + ". Provide it by using NewSerializerForChannels bean")),
            deserializers.getForChannel(topic)
                .orElseThrow(() -> new RuntimeException("There is no serializer for topic: " + topic +
                    ". Provide it by using NewDeserializerForChannels bean"))
        );
    }
}
