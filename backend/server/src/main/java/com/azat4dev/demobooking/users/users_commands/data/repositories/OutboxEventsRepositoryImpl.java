package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.data.entities.OutboxEventData;
import com.azat4dev.demobooking.users.users_commands.data.repositories.jpa.JpaOutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;

public class OutboxEventsRepositoryImpl implements OutboxEventsRepository {

    public final TimeProvider timeProvider;
    public final JpaOutboxEventsRepository jpaOutboxEventsRepository;
    public final DomainEventSerializer domainEventSerializer;

    public OutboxEventsRepositoryImpl(
        TimeProvider timeProvider,
        DomainEventSerializer domainEventSerializer,
        JpaOutboxEventsRepository jpaOutboxEventsRepository
    ) {
        this.timeProvider = timeProvider;
        this.jpaOutboxEventsRepository = jpaOutboxEventsRepository;
        this.domainEventSerializer = domainEventSerializer;
    }

    @Override
    public void publish(DomainEvent event) {

        final var eventRecord = new OutboxEventData(
            this.timeProvider.currentTime(),
            event.getType(),
            this.domainEventSerializer.serialize(event)
        );

        this.jpaOutboxEventsRepository.saveAndFlush(eventRecord);
    }
}
