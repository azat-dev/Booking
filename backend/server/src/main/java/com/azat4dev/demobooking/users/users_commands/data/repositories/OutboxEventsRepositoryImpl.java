package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.data.entities.OutboxEventData;
import com.azat4dev.demobooking.users.users_commands.data.repositories.jpa.JpaOutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxEventsRepositoryImpl implements OutboxEventsRepository {

    private final TimeProvider timeProvider;
    private final DomainEventSerializer domainEventSerializer;
    private final JpaOutboxEventsRepository jpaOutboxEventsRepository;

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
