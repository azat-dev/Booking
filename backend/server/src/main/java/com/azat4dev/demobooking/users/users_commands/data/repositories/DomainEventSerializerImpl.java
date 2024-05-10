package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

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

    @Override
    public <Payload extends Serializable, Event extends DomainEvent<Payload>> DomainEvent<Payload> deserialize(String event, Class<Event> eventClass) {
        try {
            return this.objectMapper.readValue(event, eventClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
