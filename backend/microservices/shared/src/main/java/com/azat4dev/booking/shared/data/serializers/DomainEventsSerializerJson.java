package com.azat4dev.booking.shared.data.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class DomainEventsSerializerJson implements DomainEventSerializer {

    private final ObjectMapper objectMapper;
    private final Map<Class<?>, MapPayload> mapPayloadToDTOByClass;
    private final Map<Class<?>, MapPayload> mapPayloadToDomainByClass;

    public DomainEventsSerializerJson(
        ObjectMapper objectMapper,
        MapPayload<?, ?>[] mappers
    ) {
        this.objectMapper = objectMapper;

        this.mapPayloadToDTOByClass = new HashMap<>();
        this.mapPayloadToDomainByClass = new HashMap<>();

        for (MapPayload<?, ?> mapper : mappers) {
            this.mapPayloadToDTOByClass.put(mapper.getDomainClass(), mapper);
            this.mapPayloadToDomainByClass.put(mapper.getDTOClass(), mapper);
        }
    }

    @Override
    public <E extends DomainEventPayload> String serialize(E payload) {

        final var mapper = mapPayloadToDTOByClass.get(payload.getClass());

        if (mapper == null) {
            log.atError().log("Can't find dto mapper for: {}", payload.getClass());
            throw new RuntimeException("Can't find dto mapper for: " + payload.getClass());
        }

        final var dto = mapper.toDTO(payload);

        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            log.atError().setCause(e).log("Serialization error");
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends DomainEventPayload> T deserialize(Class<T> payloadClass, String rawPayload) {

        final var dtoMapper = mapPayloadToDTOByClass.get(payloadClass);

        if (dtoMapper == null) {
            log.atError()
                .addArgument(payloadClass.getSimpleName())
                .log("Can't find dto mapper for: {}");

            throw new RuntimeException("Can't find dto mapper for: " + payloadClass.getSimpleName());
        }

        final var dtoClass = dtoMapper.getDTOClass();

        final var mapper = mapPayloadToDomainByClass.get(dtoClass);

        if (mapper == null) {
            log.atError()
                .addArgument(payloadClass.getSimpleName())
                .log("Can't find domain mapper for: {}");

            throw new RuntimeException("Can't find domain mapper for: " + payloadClass.getSimpleName());
        }

        try {
            final var dto = objectMapper.readValue(rawPayload, dtoClass);
            return payloadClass.cast(mapper.toDomain(dto));
        } catch (JsonProcessingException e) {
            log.atError().setCause(e).log("Deserialization error");
            throw new RuntimeException(e);
        }
    }
}
