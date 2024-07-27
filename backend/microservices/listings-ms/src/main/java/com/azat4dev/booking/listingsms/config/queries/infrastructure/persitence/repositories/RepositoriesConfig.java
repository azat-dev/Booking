package com.azat4dev.booking.listingsms.config.queries.infrastructure.persitence.repositories;

import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PublicListingsReadRepository;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.PrivateListingsReadRepositoryImpl;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.PublicListingsReadRepositoryImpl;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers.MapRecordToListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers.MapRecordToListingPublicDetails;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDataConfig")
@AllArgsConstructor
public class RepositoriesConfig {

    private final ListingsReadDao dao;

    @Bean
    public PrivateListingsReadRepository privateListingsReadRepository(MapRecordToListingPrivateDetails mapper) {
        return new PrivateListingsReadRepositoryImpl(dao, mapper);
    }

    @Bean
    public PublicListingsReadRepository publicListingsReadRepository(MapRecordToListingPublicDetails mapper) {
        return new PublicListingsReadRepositoryImpl(dao, mapper);
    }
}
