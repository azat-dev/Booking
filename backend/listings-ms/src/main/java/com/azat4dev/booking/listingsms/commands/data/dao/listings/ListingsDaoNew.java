package com.azat4dev.booking.listingsms.commands.data.dao.listings;

import com.azat4dev.booking.shared.domain.DomainException;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Optional;
import java.util.UUID;

public interface ListingsDaoNew {

    void addNew(ListingsRecord listing) throws Exception.ListingAlreadyExists;

    Optional<ListingsRecord> findById(UUID id);

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
