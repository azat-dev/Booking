package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.shared.domain.DomainException;

import java.util.Optional;

public interface Listings {

    void addNew(
        ListingId listingId,
        HostId hostId,
        ListingTitle title
    ) throws ListingsRepository.Exception.ListingAlreadyExists;

    Optional<Listing> getById(ListingId listingId);

    void update(Listing listing) throws Exception.ListingNotFound;

    // Exceptions

    abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class ListingNotFound extends Listings.Exception {
            public ListingNotFound() {
                super("Listing not found");
            }
        }
    }
}
