package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.config.infrastracture.bus.utils.ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CollectOneToOneRelationsOfDtoClassesAndMessageTypesForDomainEventsConfig {
    @Bean
    public ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes relations(List<MapDomainEvent> mappers) {

        return new ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes(
            mappers.stream().map(mapper -> new OneToOneRelationsOfDtoClassesAndMessageTypes.Item(
                mapper.getOriginalClass().getSimpleName(),
                mapper.getSerializedClass()
            )).toList()
        );
    }
}
