package com.azat4dev.booking.listingsms.config.common.infrastructure.api.rest;

import com.azat4dev.booking.shared.infrastructure.api.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.shared.infrastructure.api.JwtCurrentAuthenticatedUserIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiAuthConfig {
    @Bean
    CurrentAuthenticatedUserIdProvider getCurrentUserId() {
        return new JwtCurrentAuthenticatedUserIdProvider();
    }
}
