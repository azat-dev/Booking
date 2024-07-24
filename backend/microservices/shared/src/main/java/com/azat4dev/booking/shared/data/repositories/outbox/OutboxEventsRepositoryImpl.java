package com.azat4dev.booking.shared.data.repositories.outbox;

import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventData;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import com.azat4dev.booking.shared.domain.events.EventId;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Observed
@AllArgsConstructor
public class OutboxEventsRepositoryImpl implements OutboxEventsRepository {

    private final OutboxEventSerializer eventSerializer;
    private final OutboxEventsDao outboxEventsDao;
    private final DomainEventsFactory domainEventsFactory;

    @Override
    public void publish(DomainEventPayload event, String tracingInfo) {

        final var wrappedEvent = domainEventsFactory.issue(event);

        this.outboxEventsDao.put(
            new OutboxEventData(
                wrappedEvent.id().getValue(),
                wrappedEvent.issuedAt(),
                wrappedEvent.payload().getClass().getSimpleName(),
                eventSerializer.serialize(wrappedEvent.payload()),
                tracingInfo,
                false
            )
        );
    }

    @Override
    public void markAsPublished(List<EventId> eventIds) {
        outboxEventsDao.markAsPublished(eventIds.stream().map(EventId::getValue).toList());
    }

    @Override
    public List<Item> getNotPublishedItems(int limit) {

        return this.outboxEventsDao.findNotPublishedEvents(limit)
            .stream()
            .map(rc -> {
                return new Item(
                    new DomainEvent<>(
                        EventId.dangerouslyCreateFrom(rc.eventId()),
                        rc.createdAt(),
                        this.eventSerializer.deserialize(
                            rc.payload(),
                            rc.eventType()
                        )
                    ),
                    rc.tracingInfo()
                );
            }).toList();
    }
}
