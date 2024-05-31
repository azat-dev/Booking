package com.azat4dev.booking.listingsms.commands.application.config.data;

import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDaoImpl;
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
