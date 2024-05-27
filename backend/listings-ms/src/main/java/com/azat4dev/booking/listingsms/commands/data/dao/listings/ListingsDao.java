package com.azat4dev.booking.listingsms.commands.data.dao.listings;

import com.azat4dev.booking.shared.domain.DomainException;

import java.util.Optional;
import java.util.UUID;

public interface ListingsDao {

    void addNew(ListingData listing) throws Exception.ListingAlreadyExists;

    Optional<ListingData> findById(UUID id);

    // Exceptions

    class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static class ListingAlreadyExists extends Exception {
            public ListingAlreadyExists() {
                super("Listing already exists");
            }
        }
    }
}
