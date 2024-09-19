package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.SerializersForChannels;
import com.azat4dev.booking.shared.utils.TimeProvider;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

@Slf4j
@Observed
@AllArgsConstructor
public class KafkaMessageBus implements MessageBus {

    private final SerializersForChannels messageSerializers;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final TimeProvider timeProvider;

    @Override
    public <P> void publish(
        String channel,
        Optional<String> partitionKey,
        Data<P> data
    ) {
        final var time = timeProvider.currentTime();
        final var serializer = messageSerializers.getForChannel(channel)
            .orElseThrow(() -> new Exception.NoSerializerForChannel(channel));

        final var serializedMessaged = serializer.serialize(
            new Message(
                data.id(),
                data.type(),
                time,
                data.correlationId(),
                data.replyTo(),
                data.payload()
            )
        );

        final var r = new ProducerRecord<>(
            channel,
            partitionKey.orElse(null),
            serializedMessaged
        );

        kafkaTemplate.send(r);
        log.atDebug()
            .addArgument(channel)
            .addArgument(data::id)
            .addArgument(data::type)
            .addArgument(data::correlationId)
            .addArgument(data::replyTo)
            .addArgument(serializedMessaged)
            .log("Message sent: channel={} id={} type={} correlationId={} replyTo={} data={}");
    }

    // Exceptions

    public static abstract class Exception extends RuntimeException {

        private Exception(String message) {
            super(message);
        }

        public static final class NoSerializerForChannel extends Exception {

            public NoSerializerForChannel(String channel) {
                super("There is no serializer for channel: " + channel);
            }
        }
    }
}