package com.azat4dev.booking.shared.infrastructure.persistence.dao.outbox;

import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEvent;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public record OutboxEventData(
    String eventId,
    LocalDateTime createdAt,
    String eventType,
    String payload,
    @Nullable String tracingInfo,
    boolean isPublished) {

    public static OutboxEventData makeFromDomain(
        DomainEvent<?> event,
        @Nullable String tracingInfo,
        MapAnyDomainEvent mapEvent
    ) {

        return new OutboxEventData(
            event.id().getValue(),
            event.issuedAt(),
            event.payload().getClass().getSimpleName(),
            mapEvent.toDTO(event.payload()),
            tracingInfo,
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
            rs.getString("tracing_info"),
            rs.getBoolean("is_published")
        );
    }
}
