package com.azat4dev.booking.listingsms.queries.application.config.domain;

import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListings;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListingsImpl;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDomainConfig")
public class DomainConfig {

    @Bean
    PrivateListings privateListings(PrivateListingsReadRepository repository) {
        return new PrivateListingsImpl(repository);
    }
}
