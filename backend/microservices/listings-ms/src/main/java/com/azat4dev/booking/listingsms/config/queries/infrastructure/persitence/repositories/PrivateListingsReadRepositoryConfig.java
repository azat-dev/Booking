package com.azat4dev.booking.listingsms.config.queries.infrastructure.persitence.repositories;

import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.data.repositories.PrivateListingsReadRepositoryImpl;
import com.azat4dev.booking.listingsms.queries.data.repositories.mappers.MapRecordToListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.data.repositories.mappers.MapRecordToListingPrivateDetailsImpl;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDataConfig")
public class PrivateListingsReadRepositoryConfig {

    @Bean
    MapRecordToListingPrivateDetails mapRecordToListingPrivateDetails(ObjectMapper objectMapper) {
        return new MapRecordToListingPrivateDetailsImpl(objectMapper);
    }

    @Bean
    public PrivateListingsReadRepository dataConfig(ListingsReadDao dao, MapRecordToListingPrivateDetails mapper) {
        return new PrivateListingsReadRepositoryImpl(dao, mapper);
    }
}
