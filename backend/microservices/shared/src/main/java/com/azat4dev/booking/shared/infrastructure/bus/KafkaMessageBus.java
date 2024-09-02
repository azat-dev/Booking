package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.utils.TimeProvider;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Observed
@RequiredArgsConstructor
public class KafkaMessageBus<PARTITION_KEY, SERIALIZED_MESSAGE> implements MessageBus<PARTITION_KEY> {

    private final MessageSerializer<SERIALIZED_MESSAGE> messageSerializer;
    private final KafkaTemplate<PARTITION_KEY, SERIALIZED_MESSAGE> kafkaTemplate;
    private final TimeProvider timeProvider;
    private final LocalDateTimeSerializer dateTimeSerializer;

    public static final String MESSAGE_ID_HEADER = "x-message-id";
    public static final String MESSAGE_SENT_AT_HEADER = "x-message-sent-at";
    public static final String MESSAGE_TYPE_HEADER = "x-message-type";
    public static final String CORRELATION_ID_HEADER = "x-correlation-id";
    public static final String REPLY_TO_HEADER = "x-reply-to-id";

    private static byte[] toBytes(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    private final CustomTopologyFactoriesForTopics topologiesForTopics;
    private final MakeTopologyForTopic getDefaultTopologyForTopic;

    private final Properties streamsConfig;

    @Override
    public <MESSAGE> void publish(
        String topic,
        Optional<PARTITION_KEY> partitionKey,
        String messageId,
        String messageType,
        MESSAGE message,
        Optional<String> replyTo,
        Optional<String> correlationId
    ) {
        final var serializedMessaged = messageSerializer.serialize(message);

        final var r = new ProducerRecord<>(
            topic,
            partitionKey.orElse(null),
            serializedMessaged
        );

        final var time = timeProvider.currentTime();
        r.headers().add(MESSAGE_ID_HEADER, toBytes(messageId));
        r.headers().add(MESSAGE_SENT_AT_HEADER, toBytes(dateTimeSerializer.serialize(time)));
        r.headers().add(MESSAGE_TYPE_HEADER, toBytes(messageType));

        correlationId.ifPresent(c -> r.headers().add(CORRELATION_ID_HEADER, toBytes(c)));
        replyTo.ifPresent(c -> r.headers().add(REPLY_TO_HEADER, toBytes(c)));

        kafkaTemplate.send(r);
        log.atDebug()
            .addArgument(topic)
            .addArgument(messageId)
            .addArgument(messageType)
            .addArgument(correlationId)
            .addArgument(replyTo)
            .addArgument(serializedMessaged)
            .log("Message sent: topic={} id={} type={} correlationId={} replyTo={} data={}");
    }

    @Override
    public <MESSAGE> void publish(String topic, Optional<PARTITION_KEY> partitionKey, String messageId, String messageType, MESSAGE message) {
        publish(topic, partitionKey, messageId, messageType, message, Optional.empty(), Optional.empty());
    }

    @Override
    public Closeable listen(String topic, Consumer<ReceivedMessage> consumer) {
        return listen(topic, Optional.empty(), consumer);
    }

    @Override
    public Closeable listen(String topic, Set<String> messageTypes, Consumer<ReceivedMessage> consumer) {
        return listen(topic, Optional.of(messageTypes), consumer);
    }

    public Closeable listen(
        String topic,
        Optional<Set<String>> messageTypes,
        Consumer<ReceivedMessage> consumer
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
        Topology run(String topic, Optional<Set<String>> messageTypes, Consumer<ReceivedMessage> consumer);
    }
}