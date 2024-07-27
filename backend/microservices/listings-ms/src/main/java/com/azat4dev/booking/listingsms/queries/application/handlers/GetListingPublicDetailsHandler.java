package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;

public interface GetListingPublicDetailsHandler {

    ListingPublicDetails handle(GetListingPublicDetails command) throws Exception, ValidationException;

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
