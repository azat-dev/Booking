package com.azat4dev.booking.users.users_commands.data.repositories.dao;

import com.azat4dev.booking.users.users_commands.data.entities.OutboxEventData;

import java.util.List;

public interface OutboxEventsDao {

    void put(OutboxEventData event);

    List<OutboxEventData> findNotPublishedEvents(int limit);

    void markAsPublished(List<String> eventIds);
}
