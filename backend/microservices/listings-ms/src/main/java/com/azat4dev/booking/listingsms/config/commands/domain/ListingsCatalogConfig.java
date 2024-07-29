package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.listingsms.commands.domain.entities.ListingFactory;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listings;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsImpl;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExtractTraceContext;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReader;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class ListingsCatalogConfig {

    private final ExtractTraceContext extractTraceContext;
    private final UnitOfWorkFactory unitOfWorkFactory;
    private final TimeProvider timeProvider;
    private final OutboxEventsReader outboxEventsReader;

    @Bean
    Listings listingsCatalog(ListingFactory listingFactory) {
        return new ListingsImpl(
            unitOfWorkFactory,
            outboxEventsReader::trigger,
            timeProvider,
            extractTraceContext,
            listingFactory
        );
    }
}