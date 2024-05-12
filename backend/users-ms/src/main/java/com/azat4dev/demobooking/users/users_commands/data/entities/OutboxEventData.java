package com.azat4dev.demobooking.users.users_commands.data.entities;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public record OutboxEventData(
    String eventId,
    LocalDateTime createdAt,
    EventType eventType,
    String payload,
    boolean isPublished) {

    public static OutboxEventData makeFromDomain(DomainEventNew<?> event, DomainEventSerializer serializer) {

        return new OutboxEventData(
            event.id().getValue(),
            event.issuedAt(),
            switch (event.payload()) {
                case UserCreated userCreated -> EventType.USER_CREATED;
                default -> throw new IllegalStateException("Unexpected value: " + event);
            },
            serializer.serialize(event),
            false
        );
    }

    public static OutboxEventData makeFromData(ResultSet rs) throws SQLException {
        return new OutboxEventData(
            rs.getString("event_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
                .withNano(rs.getInt("created_at_nano")),
            OutboxEventData.EventType.valueOf(rs.getString("event_type")),
            rs.getString("payload"),
            rs.getBoolean("is_published")
        );
    }

    public enum EventType {
        USER_CREATED
    }
}
