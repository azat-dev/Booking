package com.azat4dev.booking.shared.data.repositories.outbox;

import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;

import javax.annotation.Nullable;
import java.util.List;

public interface OutboxEventsRepository {

    void publish(DomainEventPayload event, @Nullable String tracingInfo);

    void markAsPublished(List<EventId> eventId);

    List<Item> getNotPublishedItems(int limit);

    record Item(
        DomainEvent<?> event,
        @Nullable String tracingInfo
    ) {
    }
}