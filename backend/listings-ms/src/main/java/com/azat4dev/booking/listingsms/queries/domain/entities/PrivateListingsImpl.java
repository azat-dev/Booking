package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public final class PrivateListingsImpl implements PrivateListings {

    private final PrivateListingsReadRepository readRepository;

    @Override
    public Optional<PrivateListingDetails> findById(ListingId listingId) {
        return readRepository.findById(listingId);
    }
}
