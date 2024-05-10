package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxEventsPublisherImpl implements OutboxEventsPublisher {

    private final OutboxEventsRepository outboxEventsRepository;
    private final DomainEventsBus bus;

    @Override
    public void publishEvents() {

        while (true) {
            final var events = outboxEventsRepository.getNotPublishedEvents(100);
            if (events.isEmpty()) {
                break;
            }
            events.forEach(bus::publish);
        }
    }
}
