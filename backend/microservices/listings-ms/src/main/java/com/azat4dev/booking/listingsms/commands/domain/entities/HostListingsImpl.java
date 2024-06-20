package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public final class HostListingsImpl implements HostListings {

    private final HostId hostId;
    private final ListingsRepository repository;

    @Override
    public Optional<Listing> findById(ListingId listingId) {

        final var foundListing = repository.findById(listingId);

        return foundListing.map(listing -> {
            final var hostId = listing.getHostId();
            if (!hostId.equals(hostId)) {
                return null;
            }

            return listing;
        });
    }
}
