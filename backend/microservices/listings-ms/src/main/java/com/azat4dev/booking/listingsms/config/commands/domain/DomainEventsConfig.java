package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactoryImpl;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventsConfig {
    @Bean
    EventIdGenerator eventIdGenerator() {
        return new RandomEventIdGenerator();
    }

    @Bean
    DomainEventsFactory domainEventsFactory(
        TimeProvider timeProvider,
        EventIdGenerator eventIdGenerator
    ) {
        return new DomainEventsFactoryImpl(
            eventIdGenerator,
            timeProvider
        );
    }
}

