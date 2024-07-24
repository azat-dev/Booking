package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.shared.data.serializers.*;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Configuration
public class CommonBeansConfig {

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    Serializer<LocalDateTime, String> mapLocalDateTime() {
        return new MapLocalDateTime();
    }

    @Bean
    MapAnyDomainEvent mapAnyDomainEvent(List<MapDomainEvent<?, ?>> mappers) {
        return new MapAnyDomainEventImpl(mappers);
    }
}
