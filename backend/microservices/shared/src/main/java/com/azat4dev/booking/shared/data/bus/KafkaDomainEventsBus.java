package com.azat4dev.booking.shared.data.bus;

import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.events.*;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Slf4j
@Observed
@RequiredArgsConstructor
public class KafkaDomainEventsBus implements DomainEventsBus {

    private final GetClassForEventType getClassForEventType;
    private final GetTopic getTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DomainEventSerializer domainEventSerializer;
    private final ConcurrentKafkaListenerContainerFactory<String, String> containerFactory;
    private final TimeProvider timeProvider;
    private final EventIdGenerator eventIdGenerator;
    private final LocalDateTimeSerializer timeSerializer;

    public static final String EVENT_ID_HEADER = "event_id";
    public static final String ISSUED_AT_HEADER = "issued_at";
    public static final String EVENT_TYPE_HEADER = "event_type";

    @Override
    public void publish(DomainEventPayload event, LocalDateTime time, EventId eventId) {

        final String serializedEvent = domainEventSerializer.serialize(event);

        final var r = new ProducerRecord<>(
            getTopic.execute(event.getClass()),
            eventId.getValue(),
            serializedEvent
        );

        r.headers().add(EVENT_ID_HEADER, eventId.getValue().getBytes());
        r.headers().add(ISSUED_AT_HEADER, timeSerializer.serialize(time).getBytes());
        r.headers().add(EVENT_TYPE_HEADER, event.getClass().getSimpleName().getBytes());

        kafkaTemplate.send(r);
    }

    @Override
    public void publish(Command command) {
        this.publish(command, timeProvider.currentTime(), eventIdGenerator.generate());
    }

    @Override
    public void publish(DomainEventPayload event) {
        this.publish(
            event,
            timeProvider.currentTime(),
            eventIdGenerator.generate()
        );
    }

    @Override
    public Closeable listen(Class<DomainEventPayload> eventType, Consumer<DomainEvent<?>> listener) {

        final var logger = log;
        final var topic = getTopic.execute(eventType);
        final var container = containerFactory.createContainer(topic);
        container.getContainerProperties().setObservationEnabled(true);

        container.setupMessageListener(new AcknowledgingMessageListener<>() {

            @Override
            public void onMessage(ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment) {

                final var headers = data.headers();

                final var eventId = EventId.dangerouslyCreateFrom(
                    new String(headers.lastHeader(EVENT_ID_HEADER).value(),
                        StandardCharsets.UTF_8)
                );

                final var issuedAt = timeSerializer.deserialize(
                    new String(headers.lastHeader(ISSUED_AT_HEADER).value(),
                        StandardCharsets.UTF_8
                    )
                );

                final var eventType = new String(
                    data.headers().lastHeader(EVENT_TYPE_HEADER).value(),
                    StandardCharsets.UTF_8
                );

                final var payloadClass = getClassForEventType.execute(eventType);

                if (payloadClass == null) {
                    logger.atError()
                        .addArgument(eventType)
                        .log("Can't find class for event type: {}");
                    throw new RuntimeException("Can't find class for event type");
                }

                final var rawPayload = (String) data.value();

                final var payload = domainEventSerializer.deserialize(
                    payloadClass,
                    rawPayload
                );

                listener.accept(
                    new DomainEvent<>(
                        eventId,
                        issuedAt,
                        payload
                    )
                );

                if (acknowledgment != null) {
                    acknowledgment.acknowledge();
                }
            }
        });

        container.start();
        return container::stop;
    }

    public interface LocalDateTimeSerializer {

        String serialize(LocalDateTime time);

        LocalDateTime deserialize(String time);
    }

    @FunctionalInterface
    public interface GetClassForEventType {

        @Nullable
        Class<? extends DomainEventPayload> execute(String eventType);
    }

    @FunctionalInterface
    public interface GetTopic {
        String execute(Class<? extends DomainEventPayload> eventClass);
    }
}