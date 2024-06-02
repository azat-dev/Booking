package com.azat4dev.booking.shared.data.dao.outbox;

import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.events.DomainEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public record OutboxEventData(
    String eventId,
    LocalDateTime createdAt,
    String eventType,
    String payload,
    boolean isPublished) {

    public static OutboxEventData makeFromDomain(DomainEvent<?> event, DomainEventSerializer serializer) {

        return new OutboxEventData(
            event.id().getValue(),
            event.issuedAt(),
            event.payload().getClass().getSimpleName(),
            serializer.serialize(event),
            false
        );
    }

    public static OutboxEventData makeFromData(ResultSet rs) throws SQLException {
        return new OutboxEventData(
            rs.getString("event_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
                .withNano(rs.getInt("created_at_nano")),
            rs.getString("event_type"),
            rs.getString("payload"),
            rs.getBoolean("is_published")
        );
    }

    public enum EventType {
        USER_CREATED,
        UPDATED_USER_PHOTO,
        USER_VERIFIED_EMAIL
    }
}
