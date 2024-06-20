package com.azat4dev.booking.users.config.users_queries.infrastracture.presentation;

import com.azat4dev.booking.users.queries.infrastructure.presentation.api.rest.mappers.MapPersonalUserInfoToDTO;
import com.azat4dev.booking.users.queries.infrastructure.presentation.api.rest.mappers.MapPersonalUserInfoToDTOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    MapPersonalUserInfoToDTO mapPersonalInfoToDTO() {
        return new MapPersonalUserInfoToDTOImpl();
    }
}
