package com.azat4dev.booking.listingsms.commands.infrastructure.persistence.dao.listings;

import com.azat4dev.booking.shared.domain.DomainException;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Optional;
import java.util.UUID;

public interface ListingsDao {

    void addNew(ListingsRecord listing) throws Exception.ListingAlreadyExists;

    void update(ListingsRecord listing) throws Exception.ListingNotFound;

    Optional<ListingsRecord> findById(UUID id);

    // Exceptions

    class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static class ListingAlreadyExists extends Exception {
            public ListingAlreadyExists() {
                super("Listing already exists");
            }
        }

        public static class ListingNotFound extends Exception {
            public ListingNotFound() {
                super("Listing not found");
            }
        }
    }
}
