package com.azat4dev.booking.listingsms.config.common.infrastracture.api.rest;

import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.shared.presentation.JwtCurrentAuthenticatedUserIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiAuthConfig {
    @Bean
    CurrentAuthenticatedUserIdProvider getCurrentUserId() {
        return new JwtCurrentAuthenticatedUserIdProvider();
    }
}
