package com.azat4dev.booking.listingsms.queries.application.config.application;

import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandler;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandlerImpl;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesCommandHandlersConfig")
public class CommandHandlersConfig {

    @Bean
    GetListingPrivateDetailsHandler getListingPrivateDetailsHandler(PrivateListings privateListings) {
        return new GetListingPrivateDetailsHandlerImpl(privateListings);
    }
}
