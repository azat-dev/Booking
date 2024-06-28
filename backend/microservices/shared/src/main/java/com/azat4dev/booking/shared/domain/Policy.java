package com.azat4dev.booking.shared.domain;

import com.azat4dev.booking.shared.domain.events.DomainEvent;

public interface Policy<E extends DomainEvent> {

    void execute(E event);
}
