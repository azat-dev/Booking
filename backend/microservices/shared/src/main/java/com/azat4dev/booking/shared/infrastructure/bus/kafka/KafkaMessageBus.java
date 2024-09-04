package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializersForTopics;
import com.azat4dev.booking.shared.utils.TimeProvider;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Observed
@RequiredArgsConstructor
public class KafkaMessageBus implements MessageBus {

    private final MessageSerializersForTopics messageSerializers;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final TimeProvider timeProvider;

    private final CustomTopologyFactoriesForTopics topologiesForTopics;
    private final MakeTopologyForTopic getDefaultTopologyForTopic;

    private final Properties streamsConfig;

    @Override
    public <P> void publish(
        String topic,
        Optional<String> partitionKey,
        Data<P> data
    ) {
        final var time = timeProvider.currentTime();
        final var serializer = messageSerializers.getForTopic(topic)
            .orElseThrow(() -> new Exception.NoSerializerForTopic(topic));

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
            topic,
            partitionKey.orElse(null),
            serializedMessaged
        );

        kafkaTemplate.send(r);
        log.atDebug()
            .addArgument(topic)
            .addArgument(data::id)
            .addArgument(data::type)
            .addArgument(data::correlationId)
            .addArgument(data::replyTo)
            .addArgument(serializedMessaged)
            .log("Message sent: topic={} id={} type={} correlationId={} replyTo={} data={}");
    }

    @Override
    public Closeable listen(String topic, Consumer<Message> consumer) {
        return listen(topic, Optional.empty(), consumer);
    }

    @Override
    public Closeable listen(String topic, Set<String> messageTypes, Consumer<Message> consumer) {
        return listen(topic, Optional.of(messageTypes), consumer);
    }

    public Closeable listen(
        String topic,
        Optional<Set<String>> messageTypes,
        Consumer<Message> consumer
    ) {

        log.atDebug()
            .addArgument(topic)
            .addArgument(() -> messageTypes.orElse(null))
            .log("Open listener: topic={}, messageTypes={}");

        final var getTopology = topologiesForTopics.getForTopic(topic).orElse(getDefaultTopologyForTopic);
        final var topology = getTopology.run(topic, messageTypes, consumer);
        final var streams = new KafkaStreams(topology, streamsConfig);

        streams.start();
        return streams::close;
    }

    public interface LocalDateTimeSerializer {

        String serialize(LocalDateTime time);

        LocalDateTime deserialize(String time);
    }

    @FunctionalInterface
    public interface MakeTopologyForTopic {
        Topology run(String topic, Optional<Set<String>> messageTypes, Consumer<Message> consumer);
    }

    // Exceptions

    public static abstract class Exception extends RuntimeException {

        private Exception(String message) {
            super(message);
        }

        public static final class NoSerializerForTopic extends Exception {

            public NoSerializerForTopic(String topic) {
                super("There is no serializer for topic: " + topic);
            }
        }
    }
}