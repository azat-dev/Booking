package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetPublicListingDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.shared.domain.DomainException;

public interface GetPublicListingDetailsHandler {

    ListingPublicDetails handle(GetPublicListingDetails command) throws Exception;

    // Exceptions

    abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static class Forbidden extends Exception {
            public Forbidden() {
                super("Forbidden");
            }
        }

        public static class ListingNotFound extends Exception {
            public ListingNotFound(ListingId id) {
                super("Listing with id " + id.getValue() + " not found");
            }
        }
    }
}
