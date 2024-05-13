package com.azat4dev.demobooking.common.domain;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;

public interface CommandHandler<Command extends DomainEventNew> {

    void handle(Command command);
}
