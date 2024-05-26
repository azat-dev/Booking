package com.azat4dev.booking.users.users_commands.data.entities;

import com.azat4dev.booking.shared.domain.event.DomainEvent;
import com.azat4dev.booking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.booking.users.users_commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserVerifiedEmail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public record OutboxEventData(
    String eventId,
    LocalDateTime createdAt,
    EventType eventType,
    String payload,
    boolean isPublished) {

    public static OutboxEventData makeFromDomain(DomainEvent<?> event, DomainEventSerializer serializer) {

        return new OutboxEventData(
            event.id().getValue(),
            event.issuedAt(),
            switch (event.payload()) {
                case UserCreated userCreated -> EventType.USER_CREATED;
                case UpdatedUserPhoto inst -> EventType.UPDATED_USER_PHOTO;
                case UserVerifiedEmail inst -> EventType.USER_VERIFIED_EMAIL;
                default -> throw new IllegalStateException("Unexpected outbox event value: " + event);
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
        USER_CREATED,
        UPDATED_USER_PHOTO,
        USER_VERIFIED_EMAIL
    }
}
