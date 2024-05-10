package com.azat4dev.demobooking.users.users_commands.data.repositories.dao;

import com.azat4dev.demobooking.users.users_commands.data.entities.OutboxEventData;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public final class OutboxEventsDaoJdbc implements OutboxEventsDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<OutboxEventData> rowMapper = (rs, rowNum) -> OutboxEventData.makeFromData(rs);

    @Override
    public void put(OutboxEventData event) {

        final var sql = """
                INSERT INTO outbox_events (event_id, event_type, payload, created_at, is_published)
                VALUES (:event_id, :event_type, :payload, :created_at, :is_published)
            """;
        final var values = Map.of(
            "event_id", event.getEventId(),
            "event_type", "UserCreated",
            "payload", event.getPayload(),
            "created_at", event.getCreatedAt().toString(),
            "is_published", false
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
                    SELECT * FROM outbox_events WHERE is_published = FALSE ORDER BY created_at LIMIT :limit
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
