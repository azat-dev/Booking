package com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.DomainException;

import java.util.Optional;

public interface ListingsRepository {

    void addNew(Listing listing) throws ListingsRepository.Exception.ListingAlreadyExists;

    Optional<Listing> findById(ListingId id);

    void update(Listing listing) throws Exception.ListingNotFound;

    // Exceptions

    abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class ListingNotFound extends ListingsRepository.Exception {
            public ListingNotFound() {
                super("Listing not found");
            }
        }

        public static final class ListingAlreadyExists extends ListingsRepository.Exception {
            public ListingAlreadyExists() {
                super("Listing already exists");
            }
        }
    }
}
