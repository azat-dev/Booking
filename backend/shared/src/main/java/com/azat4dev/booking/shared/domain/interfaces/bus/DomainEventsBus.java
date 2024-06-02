package com.azat4dev.booking.shared.domain.interfaces.bus;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.function.Consumer;


public interface DomainEventsBus {

    void publish(DomainEventPayload event, LocalDateTime time, EventId eventId);

    void publish(Command command);

    void publish(DomainEventPayload event);

    Closeable listen(Class<DomainEventPayload> eventType, Consumer<DomainEvent<?>> listener);
}
