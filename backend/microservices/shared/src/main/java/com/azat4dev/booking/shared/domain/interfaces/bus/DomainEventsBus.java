package com.azat4dev.booking.shared.domain.interfaces.bus;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;

import java.io.Closeable;
import java.util.function.Consumer;


public interface DomainEventsBus {

    void publish(DomainEventPayload event, EventId eventId);

    void publish(Command command);

    void publish(DomainEventPayload event);

    <T extends DomainEventPayload> Closeable listen(Class<T> eventClass, Consumer<DomainEvent<T>> listener);
}
