package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.queries.application.commands.GetOwnListings;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.shared.domain.DomainException;

import java.util.List;

public interface GetOwnListingsHandler {

    List<ListingPrivateDetails> handle(GetOwnListings command) throws Exception.Forbidden;

    // Exceptions

    abstract class Exception extends DomainException {

        public Exception(String message) {
            super(message);
        }

        public static final class Forbidden extends Exception {
            public Forbidden() {
                super("Forbidden");
            }
        }
    }
}
