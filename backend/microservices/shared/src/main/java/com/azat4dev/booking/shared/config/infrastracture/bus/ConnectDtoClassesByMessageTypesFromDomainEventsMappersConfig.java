package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConnectDtoClassesByMessageTypesFromDomainEventsMappersConfig {
    @Bean
    public CustomizerForDtoClassesByMessageTypes customizerForDtoClassesByMessageTypesFromDomainEventsMappers(List<MapDomainEvent> mappers) {
        return dtoClassesByMessageTypes -> {
            for (MapDomainEvent<?, ?> mapper : mappers) {
                dtoClassesByMessageTypes.put(
                    mapper.getOriginalClass().getSimpleName(),
                    mapper.getSerializedClass()
                );
            }
        };
    }
}
