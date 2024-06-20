package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.listingsms.commands.domain.entities.HostListingsFactory;
import com.azat4dev.booking.listingsms.commands.domain.entities.HostListingsFactoryImpl;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.HostsImpl;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HostsConfig {

    @Bean
    HostListingsFactory hostListingsFactory(ListingsRepository listingsRepository) {
        return new HostListingsFactoryImpl(
            listingsRepository
        );
    }

    @Bean
    Hosts hosts(HostListingsFactory hostListingsFactory) {
        return new HostsImpl(
            hostListingsFactory
        );
    }
}
