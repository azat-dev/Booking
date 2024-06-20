package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalogImpl;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingIdImpl;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsPublisher;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListingsCatalogConfig {

    @Bean
    MakeNewListingId makeNewListingId() {
        return new MakeNewListingIdImpl();
    }

    @Bean
    ListingsCatalog listingsCatalog(
        UnitOfWorkFactory unitOfWorkFactory,
        TimeProvider timeProvider,
        OutboxEventsPublisher outboxEventsPublisher
    ) {
        return new ListingsCatalogImpl(
            unitOfWorkFactory,
            outboxEventsPublisher::publishEvents,
            timeProvider
        );
    }
}