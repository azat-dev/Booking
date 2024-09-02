package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.*;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Maps domain events to DTOs and vice versa.
 * Routes  domain events to the correct topic.
 * Publishes domain events to the message bus.
 * Listens to domain events from the message bus.
 * @param <PARTITION_KEY>
 */
@Slf4j
@Observed
@AllArgsConstructor
public class DefaultDomainEventsBus<PARTITION_KEY> implements DomainEventsBus {

    private final MessageBus<PARTITION_KEY> messageBus;
    private final GetInputTopicForEvent getInputTopicForEvent;
    private final GetOutputTopicForEvent getOutputTopicForEvent;
    private final GetPartitionKeyForEvent<PARTITION_KEY> getPartitionKeyForEvent;
    private final EventIdGenerator eventIdGenerator;
    private final MapAnyDomainEvent mapEvent;

    @Override
    public void publish(DomainEventPayload event, EventId eventId) {

        messageBus.publish(
            getOutputTopicForEvent.execute(event),
            getPartitionKeyForEvent.execute(event),
            eventId.getValue(),
            event.getClass().getSimpleName(),
            mapEvent.toDTO(event),
            Optional.empty(),
            Optional.empty()
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

                final var event = mapEvent.fromDTO(message.payload());

                consumer.accept(
                    new DomainEvent<T>(
                        EventId.dangerouslyCreateFrom(message.messageId()),
                        message.messageSentAt(),
                        (T) event
                    )
                );
            }
        );
    }
}