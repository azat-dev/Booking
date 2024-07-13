package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.application.commands.PublishListing;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;

public interface PublishListingHandler {

    void handle(PublishListing command) throws ValidationException, Exception.FailedToPublish, Exception.ListingNotFoundException;

    // Exception

    abstract class Exception extends DomainException {

        protected Exception(String message) {
            super(message);
        }

        public static final class ListingNotFoundException extends Exception {

            public ListingNotFoundException(String listingId) {
                super("Listing not found: " + listingId);
            }
        }

        public static final class FailedToPublish extends Exception {

            public FailedToPublish() {
                super("Failed to publish listing");
            }
        }
    }
}
