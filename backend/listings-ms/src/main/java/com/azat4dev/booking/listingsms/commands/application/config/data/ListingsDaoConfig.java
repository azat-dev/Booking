package com.azat4dev.booking.listingsms.commands.application.config.data;

import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDaoNew;
import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDaoNewImpl;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("commandsListingsDaoConfig")
public class ListingsDaoConfig {

    @Bean
    ListingsDaoNew listingsDao(
        DSLContext dslContext
    ) {
        return new ListingsDaoNewImpl(dslContext);
    }
}
