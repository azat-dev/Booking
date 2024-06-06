package com.azat4dev.booking.shared.domain.producers;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.events.EventId;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class OutboxEventsPublisherImpl implements OutboxEventsPublisher {

    private final OutboxEventsRepository outboxEventsRepository;
    private final DomainEventsBus bus;

    @Override
    public void publishEvents() {

        while (true) {
            final var events = outboxEventsRepository.getNotPublishedEvents(1);
            if (events.isEmpty()) {
                break;
            }

            events.forEach(e -> bus.publish(e.payload(), e.issuedAt(), e.id()));
            List<EventId> ids = events.stream().map(DomainEvent::id).toList();
            outboxEventsRepository.markAsPublished(ids);
        }
    }
}
