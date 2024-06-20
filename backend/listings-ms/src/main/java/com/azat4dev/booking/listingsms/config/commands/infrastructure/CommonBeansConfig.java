package com.azat4dev.booking.listingsms.config.commands.infrastructure;

import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("commandsDataConfig")
public class CommonBeansConfig {

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }
}
