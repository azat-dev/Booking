package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;

import java.io.Serializable;


public interface DomainEventSerializer {

    String serialize(DomainEvent<?> event);

    <Payload extends Serializable, Event extends DomainEvent<Payload>>
    DomainEvent<Payload> deserialize(String event, Class<Event> eventClass);
}
