package com.azat4dev.booking.listingsms.queries.domain.interfaces;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;

import java.util.Optional;

public interface PublicListingsReadRepository {

    Optional<ListingPublicDetails> findById(ListingId id) throws Exception.ListingNotPublished;

    // Exceptions
    abstract class Exception extends RuntimeException {
        protected Exception(String message) {
            super(message);
        }

        public static class ListingNotPublished extends Exception {
            public ListingNotPublished(ListingId id) {
                super("Listing isn't published: id=" + id);
            }
        }
    }
}
