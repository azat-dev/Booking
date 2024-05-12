package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEventNew;


public interface DomainEventSerializer {

    String serialize(DomainEventNew<?> event);

    DomainEventNew<?> deserialize(String event);
}
