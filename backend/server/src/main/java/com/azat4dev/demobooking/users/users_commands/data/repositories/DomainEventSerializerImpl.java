package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainEventSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

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
