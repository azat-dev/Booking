package com.azat4dev.booking.listingsms.queries.application.config.domain;

import com.azat4dev.booking.listingsms.queries.domain.entities.HostListingsFactory;
import com.azat4dev.booking.listingsms.queries.domain.entities.HostListingsFactoryImpl;
import com.azat4dev.booking.listingsms.queries.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.queries.domain.entities.HostsImpl;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDomainConfig")
public class DomainConfig {

    @Bean
    Hosts users(HostListingsFactory hostListingsFactory) {
        return new HostsImpl(hostListingsFactory);
    }

    @Bean
    HostListingsFactory userListingFactory(PrivateListingsReadRepository repository) {
        return new HostListingsFactoryImpl(repository);
    }
}
