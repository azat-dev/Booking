package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.DomainEventsSerializerImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class CommonBeansConfig {

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    DomainEventSerializer domainEventSerializer(ObjectMapper objectMapper) {
        return new DomainEventsSerializerImpl(objectMapper);
    }
}
