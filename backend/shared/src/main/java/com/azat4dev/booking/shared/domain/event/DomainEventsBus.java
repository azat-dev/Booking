package com.azat4dev.booking.shared.domain.event;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface DomainEventsBus {

    void publish(DomainEventPayload event, LocalDateTime time, EventId eventId);

    void publish(Command command);

    void publish(DomainEventPayload event);

    Closeable listen(Class<DomainEventPayload> eventType, Consumer<DomainEvent<?>> listener);
}