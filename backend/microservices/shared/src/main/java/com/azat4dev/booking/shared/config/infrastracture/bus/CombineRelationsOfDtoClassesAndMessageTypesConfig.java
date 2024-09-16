package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.config.infrastracture.bus.utils.NewRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.MutableOneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.RelationsOfDtoClassesAndMessageTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CombineRelationsOfDtoClassesAndMessageTypesConfig {

    @Bean
    RelationsOfDtoClassesAndMessageTypes relationsOfDtoClassesAndMessageTypes(List<NewRelationsOfDtoClassesAndMessageTypes> listOfNewItems) {
        var relations = new MutableOneToOneRelationsOfDtoClassesAndMessageTypes();

        for (final var itemsToAdd : listOfNewItems) {

            for (final var newItems : itemsToAdd.getItems()) {
                relations.add(newItems);
            }
        }

        return relations;
    }
}
