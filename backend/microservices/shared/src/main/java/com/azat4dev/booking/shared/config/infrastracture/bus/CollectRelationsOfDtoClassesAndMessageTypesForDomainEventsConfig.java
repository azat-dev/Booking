package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.config.infrastracture.bus.utils.NewRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.RelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CollectRelationsOfDtoClassesAndMessageTypesForDomainEventsConfig {
    @Bean
    public NewRelationsOfDtoClassesAndMessageTypes relations(List<MapDomainEvent> mappers) {

        return new NewRelationsOfDtoClassesAndMessageTypes(
            mappers.stream().map(mapper -> new RelationsOfDtoClassesAndMessageTypes.Item(
                mapper.getOriginalClass().getSimpleName(),
                mapper.getSerializedClass()
            )).toList()
        );
    }
}
