package com.azat4dev.booking.listingsms.queries.application.config.presentation;

import com.azat4dev.booking.listingsms.queries.presentation.api.mappers.MapListingPrivateDetailsToDTO;
import com.azat4dev.booking.listingsms.queries.presentation.api.mappers.MapListingPrivateDetailsToDTOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    MapListingPrivateDetailsToDTO mapListingPrivateDetailsToDTO() {
        return new MapListingPrivateDetailsToDTOImpl();
    }
}
