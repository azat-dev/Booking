package com.azat4dev.demobooking.common;

public interface Policy<Event extends DomainEventNew> {

    void execute(Event event);
}
