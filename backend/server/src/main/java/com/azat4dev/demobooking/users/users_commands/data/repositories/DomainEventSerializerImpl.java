package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DomainEventSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    public DomainEventSerializerImpl(
        ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(DomainEvent event) {
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    DomainEvent deserializeDomainEvent(String event) {
//        return null;
//    }
}
