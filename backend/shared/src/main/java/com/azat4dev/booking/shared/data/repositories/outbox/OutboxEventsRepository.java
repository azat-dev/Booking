package com.azat4dev.booking.shared.data.repositories.outbox;

import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;

import java.util.List;

public interface OutboxEventsRepository {

    void publish(DomainEventPayload event);

    void markAsPublished(List<EventId> eventId);

    List<? extends DomainEvent<?>> getNotPublishedEvents(int limit);
}