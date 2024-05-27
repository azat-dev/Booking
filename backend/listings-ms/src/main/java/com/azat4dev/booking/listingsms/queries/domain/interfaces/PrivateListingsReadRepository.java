package com.azat4dev.booking.listingsms.queries.domain.interfaces;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListingDetails;

import java.util.Optional;

public interface PrivateListingsReadRepository {

    Optional<PrivateListingDetails> findById(ListingId id);
}
