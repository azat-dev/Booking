package com.azat4dev.booking.shared.config.infrastracture.api;


import com.azat4dev.booking.shared.config.infrastracture.bus.utils.ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class BusApiEndpointsItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypesConfig {
    @Bean
    public ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes addItems(List<BusApiEndpoint> endpoints) {

        final var items = new ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes();

        for (var endpoint : endpoints) {

            final var inputMessageInfo = endpoint.getInputMessageInfo();

            Arrays.stream(inputMessageInfo)
                .forEach(messageInfo -> items.add(
                    new OneToOneRelationsOfDtoClassesAndMessageTypes.Item(
                        messageInfo.messageType(),
                        messageInfo.messageClass()
                    )
                ));

            for (var responseMessageInfo : endpoint.getResponseMessagesInfo()) {

                items.add(
                    new OneToOneRelationsOfDtoClassesAndMessageTypes.Item(
                        responseMessageInfo.messageType(),
                        responseMessageInfo.messageClass()
                    )
                );
            }
        }

        return items;
    }
}
