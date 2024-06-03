package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.application.commands.AddNewPhotoToListing;
import com.azat4dev.booking.shared.domain.DomainException;

public interface AddNewPhotoToListingHandler {

    void handle(AddNewPhotoToListing command)
        throws Exception.ListingNotFound, Exception.PhotoNotFound,
        Exception.AccessForbidden, Exception.PhotoAlreadyExists;

    // Exceptions
    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class ListingNotFound extends Exception {
            public ListingNotFound(String listingId) {
                super("Listing not found: " + listingId);
            }
        }

        public static final class PhotoNotFound extends Exception {
            public PhotoNotFound() {
                super("Photo not found");
            }
        }

        public static final class PhotoAlreadyExists extends Exception {
            public PhotoAlreadyExists() {
                super("Photo already exists");
            }
        }

        public static final class AccessForbidden extends Exception {
            public AccessForbidden() {
                super("Access forbidden");
            }
        }
    }
}
