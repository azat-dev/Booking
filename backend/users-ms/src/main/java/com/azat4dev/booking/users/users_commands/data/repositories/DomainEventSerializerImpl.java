package com.azat4dev.booking.users.users_commands.data.repositories;

import com.azat4dev.booking.shared.domain.event.DomainEvent;
import com.azat4dev.booking.users.users_commands.data.repositories.dto.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;


public class DomainEventSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    public DomainEventSerializerImpl() {

        final var objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        this.objectMapper = objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true)
            .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
    }

    @Override
    public String serialize(DomainEvent<?> event) {
        try {
            return objectMapper.writeValueAsString(DomainEventDTO.makeFrom(event));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DomainEvent<?> deserialize(String event) {
        try {
            final var dto = this.objectMapper.readValue(event, DomainEventDTO.class);
            return dto.toDomain();
        } catch (Exception e) {
            throw new RuntimeException(event.toString(), e);
        }
    }
}

