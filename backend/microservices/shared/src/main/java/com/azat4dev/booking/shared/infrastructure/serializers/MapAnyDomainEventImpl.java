package com.azat4dev.booking.shared.infrastructure.serializers;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class MapAnyDomainEventImpl implements MapAnyDomainEvent {

    private final Map<Class<?>, MapDomainEvent> mapPayloadToDTOByClass;
    private final Map<Class<?>, MapDomainEvent> mapPayloadToDomainByClass;

    public MapAnyDomainEventImpl(List<MapDomainEvent<?, ?>> mappers) {

        this.mapPayloadToDTOByClass = new HashMap<>();
        this.mapPayloadToDomainByClass = new HashMap<>();

        for (MapDomainEvent<?, ?> mapper : mappers) {
            this.mapPayloadToDTOByClass.put(mapper.getOriginalClass(), mapper);
            this.mapPayloadToDomainByClass.put(mapper.getSerializedClass(), mapper);
        }
    }

    @Override
    public <DOMAIN extends DomainEventPayload, DTO> DTO toDTO(DOMAIN dm) {
        final var mapper = mapPayloadToDTOByClass.get(dm.getClass());

        if (mapper == null) {
            log.atError().log("Can't find dto mapper for: {}", dm.getClass());
            throw new Exception.FailedSerialize("Can't find dto mapper for: " + dm.getClass());
        }

        return (DTO) mapper.serialize(dm);
    }

    @Override
    public <DTO> DomainEventPayload fromDTO(DTO dto) {
        final var mapper = mapPayloadToDomainByClass.get(dto.getClass());

        if (mapper == null) {
            final var className = dto.getClass().getSimpleName();
            log.atError()
                .addArgument(className)
                .log("Can't find domain mapper for: class={}");

            throw new Exception.FailedDeserialize("Can't find domain mapper for: class=" + className);
        }

        return mapper.deserialize(dto);
    }

    @Override
    public boolean isSupportedDTO(Class<?> dtoClass) {
        return mapPayloadToDomainByClass.containsKey(dtoClass);
    }
}
