package com.azat4dev.booking.shared.data.repositories.outbox;

import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
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
import java.util.function.Function;

@Slf4j
@Observed
@AllArgsConstructor
public class OutboxEventsRepositoryImpl implements OutboxEventsRepository {

    private final DomainEventSerializer domainEventSerializer;
    private final OutboxEventsDao outboxEventsDao;
    private final DomainEventsFactory domainEventsFactory;

    private final Function<String, Class<? extends DomainEventPayload>> getPayloadClass;

    @Override
    public void publish(DomainEventPayload event, String tracingInfo) {

        final var data = OutboxEventData.makeFromDomain(
            domainEventsFactory.issue(event),
            tracingInfo,
            this.domainEventSerializer
        );

        this.outboxEventsDao.put(data);
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
                final var payloadClass = getPayloadClass.apply(rc.eventType());
                log.atError()
                    .addArgument(rc.eventType())
                    .log("Can't get payload class for: eventType={}");

                return new Item(
                    new DomainEvent<>(
                        EventId.dangerouslyCreateFrom(rc.eventId()),
                        rc.createdAt(),
                        this.domainEventSerializer.deserialize(
                            payloadClass,
                            rc.payload()
                        )
                    ),
                    rc.tracingInfo()
                );
            }).toList();
    }
}
