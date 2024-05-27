package com.azat4dev.booking.listingsms.commands.application.config.application;

import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandler;
import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandlerImpl;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("commandsApplicationConfig")
public class ApplicationConfig {

    @Bean
    AddNewListingHandler addNewListingHandler(
        MakeNewListingId makeListingId,
        ListingsCatalog listings
    ) {
        return new AddNewListingHandlerImpl(
            makeListingId,
            listings
        );
    }
}
