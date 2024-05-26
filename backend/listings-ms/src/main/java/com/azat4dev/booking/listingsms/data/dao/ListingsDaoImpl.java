package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.commands.core.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.core.domain.values.ListingId;

import java.util.Optional;

public final class ListingsDaoImpl implements ListingsDao {

    @Override
    public void addNew(Listing listing) {

    }

    @Override
    public Optional<Listing> findById(ListingId id) {
        return Optional.empty();
    }
}
