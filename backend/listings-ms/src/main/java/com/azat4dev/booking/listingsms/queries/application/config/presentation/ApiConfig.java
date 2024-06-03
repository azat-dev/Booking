package com.azat4dev.booking.listingsms.queries.application.config.presentation;

import com.azat4dev.booking.listingsms.queries.presentation.api.mappers.*;
import com.azat4dev.booking.shared.domain.values.BaseUrl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    GetListingPhotoUrl getListingPhotoUrl(
        @Qualifier("listingsPhotoBaseUrl")
        BaseUrl baseUrl
    ) {
        return new GetListingPhotoUrlImpl(baseUrl);
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
