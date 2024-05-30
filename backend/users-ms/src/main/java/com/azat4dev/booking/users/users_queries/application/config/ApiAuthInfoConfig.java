package com.azat4dev.booking.users.users_queries.application.config;

import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.shared.presentation.JwtCurrentAuthenticatedUserIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiAuthInfoConfig {

    @Bean
    CurrentAuthenticatedUserIdProvider getCurrentUserId() {
        return new JwtCurrentAuthenticatedUserIdProvider();
    }
}
