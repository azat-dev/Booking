package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializerJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
public class JsonMessageBusSerializerConfig {

    private final ObjectMapper objectMapper;

    @Bean
    MessageSerializer<String> messageSerializer(OneToOneRelationsOfDtoClassesAndMessageTypes relations) {
        return new MessageSerializerJSON(
            objectMapper,
            relations::getDtoClass
        );
    }
}

