package com.azat4dev.demobooking.common;

public interface CommandHandler<Event extends DomainEventNew> {

    void handle(Event event);
}
