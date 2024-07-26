package com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox;

import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public final class OutboxEventSerializerJSON implements OutboxEventSerializer {

    private final ObjectMapper objectMapper;
    private final MapAnyDomainEvent mapEvent;
    private final GetDTOClassForEventType getDTOClassForEventType;

    @Override
    public DomainEventPayload deserialize(String serializedEvent, String eventType) {
        final var dtoClass = getDTOClassForEventType.run(eventType);
        if (dtoClass == null) {
            log.atError()
                .addArgument(eventType)
                .log("Can't find dto class for: eventType={}");
            throw new MessageSerializer.Exception.FailedDeserialize(
                new RuntimeException("Can't find dto class for: eventType=" + eventType)
            );
        }

        try {
            final var dto = objectMapper.readValue(serializedEvent, dtoClass);
            return mapEvent.fromDTO(dto);
        } catch (JsonProcessingException e) {
            throw new MessageSerializer.Exception.FailedDeserialize(e);
        }
    }

    @Override
    public String serialize(DomainEventPayload event) {
        try {
            final var dto = mapEvent.toDTO(event);
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new MessageSerializer.Exception.FailedSerialize(e);
        }
    }

    @FunctionalInterface
    public interface GetDTOClassForEventType {
        Class<?> run(String eventType);
    }
}
