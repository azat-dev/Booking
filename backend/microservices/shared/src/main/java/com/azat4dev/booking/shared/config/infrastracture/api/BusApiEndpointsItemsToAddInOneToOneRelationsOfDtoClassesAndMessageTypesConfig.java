package com.azat4dev.booking.shared.config.infrastracture.api;


import com.azat4dev.booking.shared.config.infrastracture.bus.utils.NewRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.RelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class BusApiEndpointsItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypesConfig {
    @Bean
    public NewRelationsOfDtoClassesAndMessageTypes addItems(List<BusApiEndpoint> endpoints) {

        final var items = new NewRelationsOfDtoClassesAndMessageTypes();

        for (var endpoint : endpoints) {

            final var inputMessageInfo = endpoint.getInputMessageInfo();

            Arrays.stream(inputMessageInfo)
                .forEach(messageInfo -> items.add(
                    new RelationsOfDtoClassesAndMessageTypes.Item(
                        messageInfo.messageType(),
                        messageInfo.messageClass()
                    )
                ));

            for (var responseMessageInfo : endpoint.getResponseMessagesInfo()) {

                items.add(
                    new RelationsOfDtoClassesAndMessageTypes.Item(
                        responseMessageInfo.messageType(),
                        responseMessageInfo.messageClass()
                    )
                );
            }
        }

        return items;
    }
}
