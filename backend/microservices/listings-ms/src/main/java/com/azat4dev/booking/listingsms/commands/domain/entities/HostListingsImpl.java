package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Observed
@AllArgsConstructor
public class HostListingsImpl implements HostListings {

    private final HostId hostId;
    private final ListingsRepository repository;

    @Override
    public Optional<Listing> findById(ListingId listingId) {

        final var foundListing = repository.findById(listingId);

        return foundListing.map(listing -> {
            if (!hostId.equals(listing.getHostId())) {
                return null;
            }

            return listing;
        });
    }
}
