package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public final class UserListingsImpl implements UserListings {

    private final UserId userId;
    private final PrivateListingsReadRepository readRepository;

    @Override
    public Optional<ListingPrivateDetails> findById(ListingId listingId) {

        final var foundListing = readRepository.findById(listingId);

        if (foundListing.isEmpty()) {
            return Optional.empty();
        }

        final var listing = foundListing.get();
        final var ownerId = listing.ownerId().getValue();
        final var currentUserId = userId.value();

        if (!ownerId.equals(currentUserId)) {
            return Optional.empty();
        }

        return foundListing;
    }

    @Override
    public List<ListingPrivateDetails> listAll() {

        return readRepository.findAllByOwnerId(OwnerId.fromUserId(userId));
    }
}
