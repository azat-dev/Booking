package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.utils.TimeProvider;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Slf4j
@Observed
@RequiredArgsConstructor
public class KafkaMessageBus<PARTITION_KEY, SERIALIZED_MESSAGE> implements MessageBus<PARTITION_KEY> {

    private final GetNumberOfConsumersForTopic getNumberOfConsumersForTopic;
    private final MessageSerializer<SERIALIZED_MESSAGE> messageSerializer;
    private final KafkaTemplate<PARTITION_KEY, SERIALIZED_MESSAGE> kafkaTemplate;
    private final ConcurrentKafkaListenerContainerFactory<PARTITION_KEY, SERIALIZED_MESSAGE> containerFactory;
    private final TimeProvider timeProvider;
    private final LocalDateTimeSerializer dateTimeSerializer;

    public static final String MESSAGE_ID_HEADER = "x-message-id";
    public static final String SENT_AT_HEADER = "x-message-sent-at";
    public static final String MESSAGE_TYPE_HEADER = "x-message-type";
    public static final String CORRELATION_ID_HEADER = "x-correlation-id";

    private static byte[] toBytes(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    private static String fromBytes(byte[] key) {
        return new String(key, StandardCharsets.UTF_8);
    }

    private final Map<String, ConcurrentMessageListenerContainer<PARTITION_KEY, SERIALIZED_MESSAGE>> containersForTopics = new HashMap<>();
    private final Map<String, CopyOnWriteArrayList<ListenerData>> listenersForTopics = new HashMap<>();

    public <MESSAGE> void publish(
        String topic,
        Optional<PARTITION_KEY> partitionKey,
        Optional<String> correlationId,
        String messageId,
        String messageType,
        MESSAGE message
    ) {
        final var serializedMessaged = messageSerializer.serialize(message);

        final var r = new ProducerRecord<>(
            topic,
            partitionKey.orElse(null),
            serializedMessaged
        );

        final var time = timeProvider.currentTime();
        r.headers().add(MESSAGE_ID_HEADER, toBytes(messageId));
        r.headers().add(SENT_AT_HEADER, toBytes(dateTimeSerializer.serialize(time)));
        r.headers().add(MESSAGE_TYPE_HEADER, toBytes(messageType));

        correlationId.ifPresent(c -> r.headers().add(CORRELATION_ID_HEADER, toBytes(c)));

        kafkaTemplate.send(r);
        log.atDebug()
            .addArgument(topic)
            .addArgument(messageId)
            .addArgument(messageType)
            .addArgument(serializedMessaged)
            .log("Message sent: topic={} id={} type={} data={}");
    }

    @Override
    public Closeable listen(String topic, Consumer<ReceivedMessage> consumer) {
        return listen(topic, Optional.empty(), consumer);
    }

    @Override
    public Closeable listen(String topic, Set<String> messageTypes, Consumer<ReceivedMessage> consumer) {
        return listen(topic, Optional.of(messageTypes), consumer);
    }

    @Nullable
    private CopyOnWriteArrayList<ListenerData> getListenersForTopic(String topic) {
        synchronized (listenersForTopics) {
            return listenersForTopics.get(topic);
        }
    }

    private void removeListener(String topic, ListenerData listener) {

        synchronized (listenersForTopics) {
            final var listeners = getListenersForTopic(topic);

            if (listeners != null) {
                listeners.remove(listener);
            }

            if (listeners == null || listeners.isEmpty()) {
                final var container = containersForTopics.get(topic);
                if (container == null) {
                    return;
                }

                container.stop(true);
            }
        }
    }

    private ConcurrentMessageListenerContainer<PARTITION_KEY, SERIALIZED_MESSAGE> makeNewContainer(String topic) {
        final var container = containerFactory.createContainer(topic);
        container.getContainerProperties().setObservationEnabled(true);
        container.setConcurrency(getNumberOfConsumersForTopic.run(topic));

        container.setupMessageListener((AcknowledgingMessageListener<String, SERIALIZED_MESSAGE>) (data, acknowledgment) -> {

            final var listeners = getListenersForTopic(topic);
            if (listeners == null) {
                return;
            }

            final var headers = data.headers();
            final var messageType = fromBytes(headers.lastHeader(MESSAGE_TYPE_HEADER).value());

            if (listeners.stream().noneMatch(l -> l.messageTypes.isEmpty() || l.messageTypes.get().contains(messageType))) {
                if (acknowledgment != null) {
                    acknowledgment.acknowledge();
                }
                return;
            }

            final var messageId = fromBytes(headers.lastHeader(MESSAGE_ID_HEADER).value());

            final var correlationIdHeader = headers.lastHeader(CORRELATION_ID_HEADER);
            Optional<String> correlationId = Optional.empty();
            if (correlationIdHeader != null) {
                correlationId = Optional.of(fromBytes(correlationIdHeader.value()));
            }

            final var messageSentAt = dateTimeSerializer.deserialize(
                fromBytes(headers.lastHeader(SENT_AT_HEADER).value())
            );

            final var message = messageSerializer.deserialize(data.value(), messageType);

            final var wrappedMessage = new ReceivedMessage(
                messageId,
                messageType,
                correlationId,
                messageSentAt,
                message
            );

            log.atDebug()
                .addArgument(topic)
                .addArgument(messageId)
                .addArgument(messageType)
                .addArgument(messageSentAt)
                .addArgument(message)
                .log("Received message: topic={} id={} type={} sentAt={} data={}");

            listeners.forEach(listener -> {
                final var isMessageTypeMatch = listener.messageTypes.isEmpty() || listener.messageTypes.get().contains(messageType);
                if (!isMessageTypeMatch) {
                    return;
                }

                listener.consumer.accept(wrappedMessage);
            });

            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        });

        return container;
    }

    private void addNewContainer(String topic) {

        synchronized (containersForTopics) {

            final var existingContainer = containersForTopics.get(topic);
            if (existingContainer != null) {
                return;
            }

            final var container = makeNewContainer(topic);
            containersForTopics.put(topic, container);
            container.start();
        }
    }

    public Closeable listen(
        String topic,
        Optional<Set<String>> messageTypes,
        Consumer<ReceivedMessage> consumer
    ) {

        final var listener = new ListenerData(consumer, messageTypes);

        synchronized (listenersForTopics) {
            final var listeners = this.getListenersForTopic(topic);

            if (listeners == null) {
                listenersForTopics.put(
                    topic,
                    new CopyOnWriteArrayList<>(List.of(listener))
                );
                addNewContainer(topic);
            } else {
                listeners.add(listener);
            }
        }

        return () -> {
            log.atInfo()
                .addArgument(topic)
                .log("Close listener for topic: {}");

            removeListener(topic, listener);
        };
    }

    public interface LocalDateTimeSerializer {

        String serialize(LocalDateTime time);

        LocalDateTime deserialize(String time);
    }

    private record ListenerData(
        Consumer<ReceivedMessage> consumer,
        Optional<Set<String>> messageTypes
    ) {

    }
}