package com.azat4dev.booking.shared.data.dao.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public final class OutboxEventsDaoJdbc implements OutboxEventsDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<OutboxEventData> rowMapper = (rs, rowNum) -> OutboxEventData.makeFromData(rs);

    @Override
    public void put(OutboxEventData event) {

        final var sql = """
                INSERT INTO outbox_events (event_id, event_type, payload, created_at, created_at_nano, is_published, tracing_info)
                VALUES (:event_id, :event_type, :payload, :created_at, :created_at_nano, :is_published, :tracing_info::jsonb)
            """;
        final var values = Map.of(
            "event_id", event.eventId(),
            "event_type", event.eventType(),
            "payload", event.payload(),
            "created_at", Timestamp.valueOf(event.createdAt().withNano(0)),
            "created_at_nano", event.createdAt().getNano(),
            "is_published", false,
            "tracing_info", event.tracingInfo()
        );

        jdbcTemplate.update(
            sql,
            values
        );
    }

    @Override
    public List<OutboxEventData> findNotPublishedEvents(int limit) {

        return jdbcTemplate.query(
            """
                SELECT * FROM outbox_events 
                WHERE is_published = FALSE 
                ORDER BY created_at ASC, created_at_nano ASC 
                LIMIT :limit
                """,
            Map.of("limit", limit),
            rowMapper
        );
    }

    @Override
    public void markAsPublished(List<String> eventIds) {

        final var sql = """
           UPDATE outbox_events SET is_published = TRUE WHERE event_id IN (:event_ids)
            """;

        jdbcTemplate.update(
            sql,
            Map.of("event_ids", eventIds)
        );
    }
}
