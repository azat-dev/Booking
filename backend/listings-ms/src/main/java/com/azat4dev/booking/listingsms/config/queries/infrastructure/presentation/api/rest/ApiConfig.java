package com.azat4dev.booking.listingsms.config.queries.infrastructure.presentation.api.rest;

import com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.files.properties.ListingsPhotoBucketConfigProperties;
import com.azat4dev.booking.listingsms.queries.presentation.api.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

    @Bean
    GetListingPhotoUrl getListingPhotoUrl(ListingsPhotoBucketConfigProperties bucketProperties) {
        return new GetListingPhotoUrlImpl(bucketProperties.getBaseUrl());
    }

    @Bean
    MapListingPhotoToDTO mapListingPhotoToDTO(GetListingPhotoUrl getListingPhotoUrl) {
        return new MapListingPhotoToDTOImpl(getListingPhotoUrl);
    }

    @Bean
    MapListingPrivateDetailsToDTO mapListingPrivateDetailsToDTO(
        MapListingPhotoToDTO mapListingPhoto
    ) {
        return new MapListingPrivateDetailsToDTOImpl(mapListingPhoto);
    }
}
