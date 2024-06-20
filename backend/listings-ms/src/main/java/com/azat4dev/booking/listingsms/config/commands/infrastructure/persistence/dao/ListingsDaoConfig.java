package com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.dao;

import com.azat4dev.booking.listingsms.commands.infrastructure.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.infrastructure.dao.listings.ListingsDaoImpl;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("commandsListingsDaoConfig")
public class ListingsDaoConfig {

    @Bean
    ListingsDao listingsDao(
        DSLContext dslContext
    ) {
        return new ListingsDaoImpl(dslContext);
    }
}
