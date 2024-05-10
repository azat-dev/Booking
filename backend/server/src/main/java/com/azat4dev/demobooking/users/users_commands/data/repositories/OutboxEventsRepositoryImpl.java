package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.EventId;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.data.entities.OutboxEventData;
import com.azat4dev.demobooking.users.users_commands.data.repositories.jpa.JpaOutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RequiredArgsConstructor
public class OutboxEventsRepositoryImpl implements OutboxEventsRepository {

    private final TimeProvider timeProvider;
    private final DomainEventSerializer domainEventSerializer;
    private final JpaOutboxEventsRepository jpaOutboxEventsRepository;

    @Override
    public void publish(DomainEvent<?> event) {

        final var eventRecord = OutboxEventData.makeFromDomain(event, this.domainEventSerializer);
        this.jpaOutboxEventsRepository.saveAndFlush(eventRecord);
    }

    @Override
    public void markAsPublished(EventId eventId) {
        final var eventRecord = this.jpaOutboxEventsRepository.findById(eventId.getValue()).orElseThrow();
        eventRecord.setPublished(true);
        this.jpaOutboxEventsRepository.saveAndFlush(eventRecord);
    }

    @Override
    public List<? extends DomainEvent<?>> getNotPublishedEvents(int limit) {

        final var pageRequest = PageRequest.of(0, limit);
        return this.jpaOutboxEventsRepository.findAllByPublishedFalseOrderByOrder(pageRequest)
            .stream()
            .map(rc -> rc.toDomainEvent(this.domainEventSerializer))
            .toList();
    }
}
