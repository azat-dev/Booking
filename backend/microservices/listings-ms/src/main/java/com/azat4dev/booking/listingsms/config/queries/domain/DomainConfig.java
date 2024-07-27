package com.azat4dev.booking.listingsms.config.queries.domain;

import com.azat4dev.booking.listingsms.queries.domain.entities.*;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PublicListingsReadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDomainConfig")
public class DomainConfig {

    @Bean("queries/Hosts")
    Hosts hosts(HostListingsFactory hostListingsFactory) {
        return new HostsImpl(hostListingsFactory);
    }

    @Bean("queries/hostListingsFactory")
    HostListingsFactory hostListingsFactory(PrivateListingsReadRepository repository) {
        return new HostListingsFactoryImpl(repository);
    }

    @Bean
    PublicListings publicListings(PublicListingsReadRepository repository) {
        return new PublicListingsImpl(repository);
    }
}
