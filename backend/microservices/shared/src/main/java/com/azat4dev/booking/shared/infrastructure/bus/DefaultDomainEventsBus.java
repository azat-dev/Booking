package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Observed
@AllArgsConstructor
public class DefaultDomainEventsBus implements DomainEventsBus {

    private final MessageBus messageBus;
    private final GetInputTopicForEvent getInputTopicForEvent;
    private final GetOutputTopicForEvent getOutputTopicForEvent;
    private final GetPartitionKeyForEvent getPartitionKeyForEvent;
    private final EventIdGenerator eventIdGenerator;
    private final MapAnyDomainEvent mapEvent;

    @Override
    public void publish(DomainEventPayload event, EventId eventId) {

        messageBus.publish(
            getOutputTopicForEvent.execute(event),
            getPartitionKeyForEvent.execute(event),
            MessageBus.Data.with(
                eventId.getValue(),
                event.getClass().getSimpleName(),
                mapEvent.toDTO(event)
            )
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
}