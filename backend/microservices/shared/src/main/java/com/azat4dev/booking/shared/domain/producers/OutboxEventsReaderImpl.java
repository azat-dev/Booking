package com.azat4dev.booking.shared.domain.producers;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Observed
@RequiredArgsConstructor
public class OutboxEventsReaderImpl implements OutboxEventsReader {

    private final OutboxEventsRepository outboxEventsRepository;
    private final PublishOutboxEvent publishOutboxEvent;

    @Override
    public void trigger() {

        while (true) {
            final var items = outboxEventsRepository.getNotPublishedItems(10);
            if (items.isEmpty()) {
                break;
            }

            items.forEach(item -> {
                final var event = item.event();
                final var eventId = event.id();
                publishOutboxEvent.execute(event.payload(), event.issuedAt(), eventId, item.tracingInfo());
                outboxEventsRepository.markAsPublished(List.of(eventId));
            });
        }
    }

    @Override
    public void close() {
        // do nothing
    }
}
