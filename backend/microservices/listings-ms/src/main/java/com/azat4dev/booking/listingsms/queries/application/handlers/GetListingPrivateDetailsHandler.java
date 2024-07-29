package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.shared.domain.DomainException;

public interface GetListingPrivateDetailsHandler {

    ListingPrivateDetails handle(GetListingPrivateDetails command) throws ListingNotFoundException;

    // Exceptions

    class ListingNotFoundException extends DomainException {
        public ListingNotFoundException(ListingId id) {
            super("Listing not found: id=" + id.getValue());
        }
    }
}
