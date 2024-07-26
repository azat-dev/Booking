package com.azat4dev.booking.shared.infrastructure.persistence.dao.outbox;

import java.util.List;

public interface OutboxEventsDao {

    void put(OutboxEventData event);

    List<OutboxEventData> findNotPublishedEvents(int limit);

    void markAsPublished(List<String> eventIds);
}
