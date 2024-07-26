package com.azat4dev.booking.users.config.users_queries.infrastracture.presentation;

import com.azat4dev.booking.shared.infrastructure.api.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.shared.infrastructure.api.JwtCurrentAuthenticatedUserIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiAuthInfoConfig {

    @Bean
    CurrentAuthenticatedUserIdProvider getCurrentUserId() {
        return new JwtCurrentAuthenticatedUserIdProvider();
    }
}
