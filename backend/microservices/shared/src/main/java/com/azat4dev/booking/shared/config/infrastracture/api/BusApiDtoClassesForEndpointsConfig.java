package com.azat4dev.booking.shared.config.infrastracture.api;


import com.azat4dev.booking.shared.config.infrastracture.bus.CustomizerForDtoClassesByMessageTypes;
import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BusApiDtoClassesForEndpointsConfig {
    @Bean
    public CustomizerForDtoClassesByMessageTypes customizerDtoClassesByMessageTypesForBusApiEndpoints(List<BusApiEndpoint> endpoints) {
        return dtoClassesByMessageTypes -> {
            for (var endpoint : endpoints) {

                final var inputMessageInfo = endpoint.getInputMessageInfo();

                dtoClassesByMessageTypes.put(
                    inputMessageInfo.messageType(),
                    inputMessageInfo.messageClass()
                );

                for (var responseMessageInfo : endpoint.getResponseMessagesInfo()) {
                    dtoClassesByMessageTypes.put(
                        responseMessageInfo.messageType(),
                        responseMessageInfo.messageClass()
                    );
                }
            }
        };
    }
}
