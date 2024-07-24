package com.azat4dev.booking.shared.domain;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;

import java.time.LocalDateTime;

public interface Policy<E extends DomainEventPayload> {

    void execute(E event, EventId eventId, LocalDateTime issuedAt);

    Class<E> getEventClass();
}
