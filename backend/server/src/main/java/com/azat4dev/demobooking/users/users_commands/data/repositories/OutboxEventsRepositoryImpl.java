package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.EventId;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.data.entities.OutboxEventData;
import com.azat4dev.demobooking.users.users_commands.data.repositories.dao.OutboxEventsDao;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OutboxEventsRepositoryImpl implements OutboxEventsRepository {

    private final TimeProvider timeProvider;
    private final DomainEventSerializer domainEventSerializer;
    private final OutboxEventsDao outboxEventsDao;

    @Override
    public void publish(DomainEvent<?> event) {

        final var eventRecord = OutboxEventData.makeFromDomain(event, this.domainEventSerializer);
        this.outboxEventsDao.put(eventRecord);
    }

    @Override
    public void markAsPublished(EventId eventId) {
        outboxEventsDao.markAsPublished(List.of(eventId.getValue()));
    }

    @Override
    public List<? extends DomainEvent<?>> getNotPublishedEvents(int limit) {

        return this.outboxEventsDao.findNotPublishedEvents(limit)
            .stream()
            .map(rc -> rc.toDomainEvent(this.domainEventSerializer))
            .toList();
    }
}
