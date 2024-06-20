package com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.infrastructure.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.infrastructure.repositories.ListingsRepositoryImpl;
import com.azat4dev.booking.listingsms.commands.infrastructure.repositories.mappers.MapListingToRecord;
import com.azat4dev.booking.listingsms.commands.infrastructure.repositories.mappers.MapListingToRecordImpl;
import com.azat4dev.booking.listingsms.commands.infrastructure.repositories.mappers.MapRecordToListing;
import com.azat4dev.booking.listingsms.commands.infrastructure.repositories.mappers.MapRecordToListingImpl;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListingsRepositoryConfig {

    @Bean
    MapListingToRecord mapListingToData(ObjectMapper objectMapper) {
        return new MapListingToRecordImpl(objectMapper);
    }

    @Bean
    MapRecordToListing mapDataToListing(ObjectMapper objectMapper) {
        return new MapRecordToListingImpl(objectMapper);
    }

    @Bean
    ListingsRepository listingsRepository(
        ListingsDao listingsDao,
        TimeProvider timeProvider,
        MapListingToRecord mapListingToRecord,
        MapRecordToListing mapRecordToListing

    ) {
        return new ListingsRepositoryImpl(
            listingsDao,
            timeProvider,
            mapListingToRecord,
            mapRecordToListing
        );
    }
}
