package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;

import java.util.Optional;

public interface UserListings {

    Optional<ListingPrivateDetails> findById(ListingId listingId);
}
