package com.azat4dev.booking.shared.domain;

import com.azat4dev.booking.shared.domain.event.DomainEvent;

public interface Policy<Event extends DomainEvent> {

    void execute(Event event);
}
