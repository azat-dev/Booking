package com.azat4dev.demobooking.users.users_commands.data.entities;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "outbox_events")
public class OutboxEventData {

    @Column(name = "event_order", nullable = false)
    private long order;

    @Id
    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "is_published", nullable = false)
    private boolean isPublished;

    public OutboxEventData(
        String eventId,
        LocalDateTime createdAt,
        EventType eventType,
        String payload,
        boolean isPublished
    ) {
        this.order = Instant.now().toEpochMilli();
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.eventType = eventType;
        this.payload = payload;
        this.isPublished = isPublished;
    }

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

    public DomainEvent<?> toDomainEvent(DomainEventSerializer serializer) {
        return switch (this.eventType) {
            case USER_CREATED -> serializer.deserialize(this.payload, UserCreated.class);
        };
    }

    enum EventType {
        USER_CREATED
    }
}
