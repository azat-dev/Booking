package com.azat4dev.booking.shared.infrastructure.bus;

import lombok.AllArgsConstructor;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@AllArgsConstructor
public class BusMessageSerde implements Serde<MessageBus.ReceivedMessage> {

    private final MessageSerializer<String> messageSerializer;
    private final KafkaMessageBus.LocalDateTimeSerializer dateTimeSerializer;

    private static String fromBytes(byte[] key) {
        return new String(key, StandardCharsets.UTF_8);
    }

    @Override
    public Serializer<MessageBus.ReceivedMessage> serializer() {
        return null;
    }

    @Override
    public Deserializer<MessageBus.ReceivedMessage> deserializer() {
        return new Deserializer<MessageBus.ReceivedMessage>() {
            @Override
            public MessageBus.ReceivedMessage deserialize(String topic, byte[] data) {
                throw new UnsupportedOperationException("Not implemented");
            }

            @Override
            public MessageBus.ReceivedMessage deserialize(String topic, Headers headers, byte[] data) {
                final var messageType = fromBytes(headers.lastHeader(KafkaMessageBus.MESSAGE_TYPE_HEADER).value());
                final var messageId = fromBytes(headers.lastHeader(KafkaMessageBus.MESSAGE_ID_HEADER).value());

                final var correlationIdHeader = headers.lastHeader(KafkaMessageBus.CORRELATION_ID_HEADER);
                Optional<String> correlationId = Optional.empty();
                if (correlationIdHeader != null) {
                    correlationId = Optional.of(fromBytes(correlationIdHeader.value()));
                }

                final var replyToHeader = headers.lastHeader(KafkaMessageBus.REPLY_TO_HEADER);
                Optional<String> replyTo = Optional.empty();

                if (replyToHeader != null) {
                    replyTo = Optional.of(fromBytes(replyToHeader.value()));
                }

                final var messageSentAt = dateTimeSerializer.deserialize(
                    fromBytes(headers.lastHeader(KafkaMessageBus.MESSAGE_SENT_AT_HEADER).value())
                );

                final var messageData = fromBytes(data);
                final var message = messageSerializer.deserialize(messageData, messageType);

                return new MessageBus.ReceivedMessage(
                    messageId,
                    messageType,
                    correlationId,
                    replyTo,
                    messageSentAt,
                    message
                );
            }
        };
    }
}
