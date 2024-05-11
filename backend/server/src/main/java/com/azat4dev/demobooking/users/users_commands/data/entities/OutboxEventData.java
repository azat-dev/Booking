package com.azat4dev.demobooking.users.users_commands.data.entities;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public record OutboxEventData(
    String eventId,
    LocalDateTime createdAt,
    EventType eventType,
    String payload,
    boolean isPublished) {

    public static OutboxEventData makeFromDomain(DomainEvent<?> event, DomainEventSerializer serializer) {

        return new OutboxEventData(
            event.getId().getValue(),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestampMs()), ZoneOffset.UTC),
            switch (event) {
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
            rs.getTimestamp("created_at").toLocalDateTime(),
            OutboxEventData.EventType.valueOf(rs.getString("event_type")),
            rs.getString("payload"),
            rs.getBoolean("is_published")
        );
    }

    public DomainEvent<?> toDomainEvent(DomainEventSerializer serializer) {
        return switch (this.eventType) {
            case USER_CREATED -> serializer.deserialize(this.payload, UserCreated.class);
        };
    }

    public enum EventType {
        USER_CREATED
    }
}
