package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.EventId;

import java.util.List;

public interface OutboxEventsRepository {

    void publish(DomainEvent<?> event);

    void markAsPublished(List<EventId> eventId);

    List<? extends DomainEvent<?>> getNotPublishedEvents(int limit);
}