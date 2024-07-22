package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetPublicListingDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.shared.domain.DomainException;

public interface GetPublicListingDetailsHandler {

    ListingPublicDetails handle(GetPublicListingDetails command) throws ListingNotFoundException;

    // Exceptions

    class ListingNotFoundException extends DomainException {
        public ListingNotFoundException(ListingId id) {
            super("Listing with id " + id.getValue() + " not found");
        }
    }
}
