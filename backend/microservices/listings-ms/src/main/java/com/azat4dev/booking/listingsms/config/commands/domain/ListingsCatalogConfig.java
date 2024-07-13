package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalogImpl;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingIdImpl;
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
    MakeNewListingId makeNewListingId() {
        return new MakeNewListingIdImpl();
    }

    @Bean
    ListingsCatalog listingsCatalog() {
        return new ListingsCatalogImpl(
            unitOfWorkFactory,
            outboxEventsReader::trigger,
            timeProvider,
            extractTraceContext
        );
    }
}