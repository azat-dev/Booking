package com.azat4dev.demobooking.common.domain;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;

public interface Policy<Event extends DomainEventNew> {

    void execute(Event event);
}
