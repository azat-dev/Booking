package com.azat4dev.booking.shared.config.infrastracture.serializers;

import com.azat4dev.booking.shared.infrastructure.serializers.MapLocalDateTime;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DefaultTimeSerializerConfig {

    @Bean
    Serializer<LocalDateTime, String> mapLocalDateTime() {
        return new MapLocalDateTime();
    }
}
