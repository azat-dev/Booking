package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.common.domain.event.EventId;

import java.util.List;

public interface OutboxEventsRepository {

    void publish(DomainEventPayload event);

    void markAsPublished(List<EventId> eventId);

    List<? extends DomainEventNew<?>> getNotPublishedEvents(int limit);
}