package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public final class HostListingsImpl implements HostListings {

    private final HostId hostId;
    private final PrivateListingsReadRepository readRepository;

    @Override
    public Optional<ListingPrivateDetails> findById(ListingId listingId) {

        final var foundListing = readRepository.findById(listingId);

        if (foundListing.isEmpty()) {
            return Optional.empty();
        }

        final var listing = foundListing.get();
        final var hostId = listing.hostId().getValue();

        if (!listing.hostId().equals(hostId)) {
            return Optional.empty();
        }

        return foundListing;
    }

    @Override
    public List<ListingPrivateDetails> listAll() {

        return readRepository.findAllByHostId(hostId);
    }
}
