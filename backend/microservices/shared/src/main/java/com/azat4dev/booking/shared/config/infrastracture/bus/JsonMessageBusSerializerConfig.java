package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializerJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Import({DefaultMapAnyDomainEventConfig.class, ConnectDtoClassesByMessageTypesFromDomainEventsMappersConfig.class})
@Configuration
@AllArgsConstructor
public class JsonMessageBusSerializerConfig {

    private final ObjectMapper objectMapper;

    @Bean
    DtoClassesByEventTypes dtoClassesByEventTypes(List<CustomizerForDtoClassesByMessageTypes> customizers) {
        final var map = new HashMap<String, Class<?>>();

        customizers.forEach(customizer -> {
            customizer.customize(map);
        });

        return new DtoClassesByEventTypes(map);
    }

    @Bean
    MessageSerializer<String> messageSerializer(DtoClassesByEventTypes dtoClassesByEventTypes) {
        return new MessageSerializerJSON(
            objectMapper,
            dtoClassesByEventTypes
        );
    }
}

