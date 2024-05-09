package com.azat4dev.demobooking.users.users_commands.data.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Nonnull
    private String payload;

    public OutboxEvent(
        LocalDateTime createdAt,
        String eventType,
        String payload
    ) {
        this.createdAt = createdAt;
        this.eventType = eventType;
        this.payload = payload;
    }
}
