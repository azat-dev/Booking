package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;

import java.util.List;
import java.util.Optional;

public interface HostListings {

    Optional<ListingPrivateDetails> findById(ListingId listingId);

    List<ListingPrivateDetails> listAll();
}
