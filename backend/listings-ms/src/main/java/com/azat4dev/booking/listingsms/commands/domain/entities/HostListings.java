package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;

import java.util.Optional;

public interface HostListings {

    Optional<Listing> findById(ListingId listingId);
}
