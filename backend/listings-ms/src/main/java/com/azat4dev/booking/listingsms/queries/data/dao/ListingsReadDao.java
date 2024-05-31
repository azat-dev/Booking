package com.azat4dev.booking.listingsms.queries.data.dao;

import org.jooq.generated.tables.records.ListingsRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingsReadDao {

    Optional<ListingsRecord> findById(UUID listingId);

    List<ListingsRecord> findAllByOwnerId(UUID ownerId);
}
