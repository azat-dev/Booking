package com.azat4dev.booking.shared.config.infrastracture.persistence.repositories.outbox;

import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMapAnyDomainEventConfig;
import com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox.OutboxEventSerializer;
import com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox.OutboxEventSerializerJSON;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Import(DefaultMapAnyDomainEventConfig.class)
@Configuration
public class JSONOutboxEventSerializerConfig {

    private final ObjectMapper objectMapper;
    private final MapAnyDomainEvent mapEvent;
    private final Map<String, Class<?>> dtoClassesByNames;

    public JSONOutboxEventSerializerConfig(
        ObjectMapper objectMapper,
        MapAnyDomainEvent mapEvent,
        List<MapDomainEvent<?, ?>> domainEventsMappers
    ) {
        this.objectMapper = objectMapper;
        this.mapEvent = mapEvent;

        this.dtoClassesByNames = domainEventsMappers.stream()
            .collect(Collectors.toMap(v -> v.getOriginalClass().getSimpleName(),
                MapDomainEvent::getSerializedClass
            ));
    }

    @Bean
    OutboxEventSerializer eventSerializer() {

        return new OutboxEventSerializerJSON(
            objectMapper,
            mapEvent,
            eventType -> {

                final var clazz = dtoClassesByNames.get(eventType);

                if (clazz == null) {
                    log.atError()
                        .addArgument(eventType)
                        .log("Event type {} not found");
                }

                return clazz;
            });
    }

}
