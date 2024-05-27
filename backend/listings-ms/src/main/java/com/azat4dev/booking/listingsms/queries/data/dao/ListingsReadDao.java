package com.azat4dev.booking.listingsms.queries.data.dao;

import java.util.Optional;
import java.util.UUID;

public interface ListingsReadDao {

    Optional<ListingRecord> findById(UUID listingId);
}
