package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializer;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializer;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

@AllArgsConstructor
public class BusMessageSerde implements Serde<Message> {

    private final MessageSerializer messageSerializer;
    private final MessageDeserializer messageDeserializer;

    @Override
    public Serializer<Message> serializer() {

        return new Serializer<Message>() {
            @Override
            public byte[] serialize(String topic, Message data) {
                return messageSerializer.serialize(data);
            }
        };
    }

    @Override
    public Deserializer<Message> deserializer() {
        return new Deserializer<Message>() {
            @Override
            public Message deserialize(String topic, byte[] data) {
                return messageDeserializer.deserialize(data);
            }
        };
    }
}
