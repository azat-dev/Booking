package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.listingsms.commands.application.handlers.photo.MakeNewListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingFactory;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingFactoryImpl;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingIdImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListingFactoryConfig {

    @Bean
    ListingFactory listingFactory(MakeNewListingPhoto makeNewListingPhoto) {
        return new ListingFactoryImpl(makeNewListingPhoto);
    }

    @Bean
    MakeNewListingId makeNewListingId() {
        return new MakeNewListingIdImpl();
    }
}
