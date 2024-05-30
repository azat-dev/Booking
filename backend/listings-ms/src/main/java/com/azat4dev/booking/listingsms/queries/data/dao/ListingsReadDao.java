package com.azat4dev.booking.listingsms.queries.data.dao;

import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingsReadDao {

    Optional<ListingRecord> findById(UUID listingId);

    List<ListingRecord> findAllByOwnerId(UUID ownerId);
}
