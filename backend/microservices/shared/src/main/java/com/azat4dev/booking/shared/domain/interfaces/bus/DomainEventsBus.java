package com.azat4dev.booking.shared.domain.interfaces.bus;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;


public interface DomainEventsBus {

    void publish(DomainEventPayload event, EventId eventId);

    void publish(Command command);

    void publish(DomainEventPayload event);
}
