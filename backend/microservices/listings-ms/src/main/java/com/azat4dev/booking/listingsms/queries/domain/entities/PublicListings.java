package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.DomainException;

import java.util.Optional;

public interface PublicListings {
    Optional<ListingPublicDetails> findById(ListingId listingId) throws Exception.ListingNotPublished;

    abstract class Exception extends DomainException {

        protected Exception(String message) {
            super(message);
        }

        public static class ListingNotPublished extends Exception {
            public ListingNotPublished(ListingId id) {
                super("Listing not published: " + id);
            }
        }
    }
}
