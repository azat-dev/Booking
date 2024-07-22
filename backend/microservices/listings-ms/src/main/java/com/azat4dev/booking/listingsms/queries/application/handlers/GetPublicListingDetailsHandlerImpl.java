package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.queries.application.commands.GetPublicListingDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.PublicListings;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Observed
@AllArgsConstructor
public class GetPublicListingDetailsHandlerImpl implements GetPublicListingDetailsHandler {

    private final PublicListings listings;

    @Override
    public ListingPublicDetails handle(GetPublicListingDetails command) throws ListingNotFoundException {

        return null;
    }
}
