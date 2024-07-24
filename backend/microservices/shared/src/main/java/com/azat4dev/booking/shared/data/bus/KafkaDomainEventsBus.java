package com.azat4dev.booking.shared.data.bus;

import com.azat4dev.booking.shared.domain.events.*;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Observed
@AllArgsConstructor
public class KafkaDomainEventsBus<PARTITION_KEY> implements DomainEventsBus {

    private final MessageBus<PARTITION_KEY> messageBus;
    private final GetInputTopicForEvent getInputTopicForEvent;
    private final GetOutputTopicForEvent getOutputTopicForEvent;
    private final GetPartitionKeyForEvent<PARTITION_KEY> getPartitionKeyForEvent;
    private final GetClassForEventType getClassForEventType;
    private final EventIdGenerator eventIdGenerator;

    @Override
    public void publish(DomainEventPayload event, EventId eventId) {

        messageBus.publish(
            getOutputTopicForEvent.execute(event),
            getPartitionKeyForEvent.execute(event),
            eventId.getValue(),
            event.getClass().getSimpleName(),
            event
        );
    }

    @Override
    public void publish(Command command) {
        this.publish(command, eventIdGenerator.generate());
    }

    @Override
    public void publish(DomainEventPayload event) {
        this.publish(
            event,
            eventIdGenerator.generate()
        );
    }

    @Override
    public <T extends DomainEventPayload> Closeable listen(Class<T> eventType, Consumer<DomainEvent<T>> consumer) {

        return messageBus.listen(
            getInputTopicForEvent.execute(eventType),
            Set.of(eventType.getSimpleName()),
            message -> {

                final var eventClass = getClassForEventType.execute(message.messageType());
                if (eventClass == null) {
                    log.atError()
                        .addArgument(message.messageType())
                        .log("Can't find class for event type: type={}");
                    throw new RuntimeException("Can't find class for event type");
                }

                consumer.accept(
                    new DomainEvent<T>(
                        EventId.dangerouslyCreateFrom(message.messageId()),
                        message.messageSentAt(),
                        (T) message.payload()
                    )
                );
            }
        );
    }
}