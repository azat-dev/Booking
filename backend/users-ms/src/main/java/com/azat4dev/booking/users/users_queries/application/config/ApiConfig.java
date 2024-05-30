package com.azat4dev.booking.users.users_queries.application.config;

import com.azat4dev.booking.users.users_queries.presentation.api.rest.resources.mappers.MapPersonalUserInfoToDTO;
import com.azat4dev.booking.users.users_queries.presentation.api.rest.resources.mappers.MapPersonalUserInfoToDTOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    MapPersonalUserInfoToDTO mapPersonalInfoToDTO() {
        return new MapPersonalUserInfoToDTOImpl();
    }
}
