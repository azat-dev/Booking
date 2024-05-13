package com.azat4dev.demobooking.users.users_commands.domain.producers;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
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

            events.forEach(bus::publish);
            List<EventId> ids = events.stream().map(DomainEventNew::id).toList();
            outboxEventsRepository.markAsPublished(ids);
        }
    }
}
