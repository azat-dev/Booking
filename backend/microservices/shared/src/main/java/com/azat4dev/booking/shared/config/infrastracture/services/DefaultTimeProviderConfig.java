package com.azat4dev.booking.shared.config.infrastracture.services;

import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultTimeProviderConfig {

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }
}
