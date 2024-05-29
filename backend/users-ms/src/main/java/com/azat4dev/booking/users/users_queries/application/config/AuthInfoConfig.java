package com.azat4dev.booking.users.users_queries.application.config;

import com.azat4dev.booking.users.users_queries.presentation.api.utils.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.users.users_queries.presentation.api.utils.CurrentAuthenticatedUserIdProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthInfoConfig {

    @Bean
    CurrentAuthenticatedUserIdProvider getCurrentUserId() {
        return new CurrentAuthenticatedUserIdProviderImpl();
    }
}
