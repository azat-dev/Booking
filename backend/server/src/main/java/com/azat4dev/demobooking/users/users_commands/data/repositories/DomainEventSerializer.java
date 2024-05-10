package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;


public interface DomainEventSerializer {

    String serialize(DomainEvent event);

//    DomainEvent deserialize(String event);
}
