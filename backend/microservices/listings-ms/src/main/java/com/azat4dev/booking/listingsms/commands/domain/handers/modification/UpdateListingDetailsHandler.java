package com.azat4dev.booking.listingsms.commands.domain.handers.modification;

import com.azat4dev.booking.listingsms.commands.domain.commands.UpdateListingDetails;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;

public interface UpdateListingDetailsHandler {

    void handle(UpdateListingDetails command) throws Exception.ListingNotFound, Exception.AccessForbidden, ValidationException;

    // Exceptions
    abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class ListingNotFound extends Exception {
            public ListingNotFound() {
                super("Listing not found");
            }
        }

        public static final class AccessForbidden extends Exception {
            public AccessForbidden() {
                super("Access forbidden");
            }
        }
    }
}
