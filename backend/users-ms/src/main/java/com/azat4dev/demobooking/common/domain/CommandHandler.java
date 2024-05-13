package com.azat4dev.demobooking.common.domain;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;

public interface CommandHandler<Event extends DomainEventNew> {

    void handle(Event event);
}
