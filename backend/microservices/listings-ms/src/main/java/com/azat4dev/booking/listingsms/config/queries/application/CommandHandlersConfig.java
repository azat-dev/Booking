package com.azat4dev.booking.listingsms.config.queries.application;

import com.azat4dev.booking.listingsms.queries.application.handlers.*;
import com.azat4dev.booking.listingsms.queries.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.queries.domain.entities.PublicListings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesCommandHandlersConfig")
public class CommandHandlersConfig {

    @Bean
    GetListingPrivateDetailsHandler getListingPrivateDetailsHandler(Hosts hosts) {
        return new GetListingPrivateDetailsHandlerImpl(hosts);
    }

    @Bean
    GetOwnListingsHandler getOwnListingsHandler(Hosts hosts) {
        return new GetOwnListingsHandlerImpl(hosts);
    }

    @Bean
    GetListingPublicDetailsHandler getListingPublicDetailsHandler(PublicListings publicListings) {
        return new GetListingPublicDetailsHandlerImpl(publicListings);
    }
}
