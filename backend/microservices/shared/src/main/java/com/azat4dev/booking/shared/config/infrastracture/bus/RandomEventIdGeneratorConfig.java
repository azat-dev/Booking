package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomEventIdGeneratorConfig {

    @Bean
    EventIdGenerator eventIdGenerator() {
        return new RandomEventIdGenerator();
    }
}
