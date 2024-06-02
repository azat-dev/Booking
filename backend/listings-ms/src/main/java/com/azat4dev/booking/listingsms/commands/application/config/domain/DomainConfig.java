package com.azat4dev.booking.listingsms.commands.application.config.domain;

import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalogImpl;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingIdImpl;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactoryImpl;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    ListingsCatalog listingsCatalog(
        UnitOfWorkFactory unitOfWorkFactory,
        TimeProvider timeProvider
    ) {
        return new ListingsCatalogImpl(unitOfWorkFactory, timeProvider);
    }

    @Bean
    EventIdGenerator eventIdGenerator() {
        return new RandomEventIdGenerator();
    }

    @Bean
    DomainEventsFactory domainEventsFactory(
        TimeProvider timeProvider,
        EventIdGenerator eventIdGenerator
    ) {
        return new DomainEventsFactoryImpl(
            eventIdGenerator,
            timeProvider
        );
    }

    @Bean
    MakeNewListingId makeNewListingId() {
        return new MakeNewListingIdImpl();
    }
}
