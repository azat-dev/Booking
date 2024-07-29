package com.azat4dev.booking.listingsms.config.commands.application;

import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandler;
import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandlerImpl;
import com.azat4dev.booking.listingsms.commands.application.handlers.PublishListingHandler;
import com.azat4dev.booking.listingsms.commands.application.handlers.PublishListingHandlerImpl;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listings;
import com.azat4dev.booking.listingsms.commands.domain.handers.modification.UpdateListingDetailsHandler;
import com.azat4dev.booking.listingsms.commands.domain.handers.modification.UpdateListingDetailsHandlerImpl;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.shared.application.GlobalControllerExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration("commandsApplicationConfig")
@Import(GlobalControllerExceptionHandler.class)
public class ListingsCommandHandlersConfig {

    @Bean
    AddNewListingHandler addNewListingHandler(
        MakeNewListingId makeListingId,
        Listings listings
    ) {
        return new AddNewListingHandlerImpl(
            makeListingId,
            listings
        );
    }

    @Bean
    UpdateListingDetailsHandler updateListingDetailsHandler(
        Hosts hosts,
        Listings listings
    ) {
        return new UpdateListingDetailsHandlerImpl(
            hosts,
            listings
        );
    }

    @Bean
    PublishListingHandler publishListingHandler(
        Hosts hosts,
        Listings listings) {
        return new PublishListingHandlerImpl(
            hosts,
            listings
        );
    }
}
