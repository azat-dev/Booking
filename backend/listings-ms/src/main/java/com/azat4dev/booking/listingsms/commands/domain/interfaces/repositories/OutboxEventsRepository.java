package com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories;

import com.azat4dev.booking.shared.domain.event.DomainEvent;
import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.event.EventId;

import java.util.List;

public interface OutboxEventsRepository {

    void publish(DomainEventPayload event);

    void markAsPublished(List<EventId> eventId);

    List<? extends DomainEvent<?>> getNotPublishedEvents(int limit);
}