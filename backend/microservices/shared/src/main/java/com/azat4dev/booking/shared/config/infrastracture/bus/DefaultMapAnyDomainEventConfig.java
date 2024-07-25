package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.data.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.data.serializers.MapAnyDomainEventImpl;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class DefaultMapAnyDomainEventConfig {

    @Bean
    MapAnyDomainEvent mapAnyDomainEvent(List<MapDomainEvent<?, ?>> mappers) {
        return new MapAnyDomainEventImpl(mappers);
    }
}
