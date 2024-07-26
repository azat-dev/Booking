package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializerJSON;
import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializerJSONWithDomainEvents;
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
public class JsonMessageBusSerializerConfig {

    private final ObjectMapper objectMapper;
    private final Map<String, Class<?>> dtoClassesByEventTypes;

    public JsonMessageBusSerializerConfig(
        ObjectMapper objectMapper,
        List<MapDomainEvent<?, ?>> mappers
    ) {
        this.objectMapper = objectMapper;
        this.dtoClassesByEventTypes = getDTOClassesByEventTypes(mappers);
    }


    private static Map<String, Class<?>> getDTOClassesByEventTypes(
        List<MapDomainEvent<?, ?>> mappers
    ) {
        return mappers.stream()
            .collect(
                Collectors.toMap(
                    v -> v.getOriginalClass().getSimpleName(),
                    v -> v.getSerializedClass()
                )
            );
    }

    @Bean
    MessageSerializerJSON.GetClassForMessageType getClassForMessageType() {
        return eventType -> {
            final var dtoClass = dtoClassesByEventTypes.get(eventType);
            if (dtoClass == null) {
                log.atError()
                    .addArgument(eventType)
                    .log("No dto class not found for event type: {}");
                return null;
            }
            return dtoClass;
        };
    }

    @Bean
    MessageSerializer<String> messageSerializer(
        MapAnyDomainEvent mapAnyDomainEvent,
        MessageSerializerJSON.GetClassForMessageType getDtoClassForMessageType
    ) {

        final var jsonMessageSerializer = new MessageSerializerJSON(
            objectMapper,
            getDtoClassForMessageType
        );

        return new MessageSerializerJSONWithDomainEvents(
            jsonMessageSerializer,
            mapAnyDomainEvent
        );
    }
}

