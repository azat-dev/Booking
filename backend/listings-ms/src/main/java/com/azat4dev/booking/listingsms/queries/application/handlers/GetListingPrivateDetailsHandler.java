package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListingDetails;
import com.azat4dev.booking.shared.domain.DomainException;

public interface GetListingPrivateDetailsHandler {

    PrivateListingDetails handle(GetListingPrivateDetails command) throws ListingNotFoundException;

    // Exceptions

    class ListingNotFoundException extends DomainException {
        public ListingNotFoundException(ListingId id) {
            super("Listing with id " + id.getValue() + " not found");
        }
    }
}
