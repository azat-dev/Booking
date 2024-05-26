package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.commands.core.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.core.domain.values.ListingId;

import java.util.Optional;

public interface ListingsDao {

    void addNew(Listing listing);

    Optional<Listing> findById(ListingId id);
}
