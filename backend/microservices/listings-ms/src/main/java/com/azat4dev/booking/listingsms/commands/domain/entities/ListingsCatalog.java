package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.shared.domain.DomainException;

public interface ListingsCatalog {

    void addNew(
        ListingId listingId,
        HostId hostId,
        ListingTitle title
    );

    void update(Listing listing) throws Exception.ListingNotFound;

    // Exceptions

    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class ListingNotFound extends Exception {
            public ListingNotFound() {
                super("Listing not found");
            }
        }
    }
}
