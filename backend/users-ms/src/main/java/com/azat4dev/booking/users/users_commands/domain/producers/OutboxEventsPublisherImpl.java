package com.azat4dev.booking.users.users_commands.domain.producers;

import com.azat4dev.booking.shared.domain.event.DomainEvent;
import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.shared.domain.event.EventId;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
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
