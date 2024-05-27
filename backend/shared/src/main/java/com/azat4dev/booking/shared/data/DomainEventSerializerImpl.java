package com.azat4dev.booking.shared.data;

import com.azat4dev.booking.shared.domain.event.DomainEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;


public final class DomainEventSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;
    private final ConvertToDTO convertToDTO;
    private final Class<? extends SerializableDomainEvent> dtoClass;

    public DomainEventSerializerImpl(ConvertToDTO convertToDTO, Class<? extends SerializableDomainEvent> dtoClass) {

        this.convertToDTO = convertToDTO;
        this.dtoClass = dtoClass;

        final var objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        this.objectMapper = objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true)
            .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
    }

    @Override
    public String serialize(DomainEvent<?> event) {
        try {
            final var dto = convertToDTO.execute(event);
            return objectMapper.writeValueAsString(dto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DomainEvent<?> deserialize(String event) {
        try {
            final var dto = this.objectMapper.readValue(event, dtoClass);
            return dto.toDomain();
        } catch (Exception e) {
            throw new RuntimeException(event, e);
        }
    }

    public interface ConvertToDTO {
        SerializableDomainEvent execute(DomainEvent<?> domainEvent);
    }
}

