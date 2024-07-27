package com.azat4dev.booking.listingsms.config.queries.infrastructure.persitence.repositories.mappers;

import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappersConfig {

    @Bean
    MapListingPhoto mapListingPhotoReadListingsRepository() {
        return new MapListingPhotoImpl();
    }

    @Bean
    MapAddress mapAddressReadListingsRepository() {
        return new MapAddressImpl();
    }

    @Bean
    MapGuestsCapacity mapGuestsCapacityReadListingsRepository() {
        return new MapGuestsCapacityImpl();
    }

    @Bean
    MapRecordToListingPrivateDetails mapRecordToListingPrivateDetails(
        MapListingPhoto mapListingPhoto,
        MapAddress mapAddress,
        MapGuestsCapacity mapGuestsCapacity,
        ObjectMapper objectMapper
    ) {
        return new MapRecordToListingPrivateDetailsImpl(
            mapListingPhoto,
            mapAddress,
            mapGuestsCapacity,
            objectMapper
        );
    }

    @Bean
    MapRecordToListingPublicDetails mapRecordToListingPublicDetails(
        MapListingPhoto mapListingPhoto,
        MapAddress mapAddress,
        MapGuestsCapacity mapGuestsCapacity,
        ObjectMapper objectMapper
    ) {
        return new MapRecordToListingPublicDetailsImpl(
            mapListingPhoto,
            mapAddress,
            mapGuestsCapacity,
            objectMapper
        );
    }
}
