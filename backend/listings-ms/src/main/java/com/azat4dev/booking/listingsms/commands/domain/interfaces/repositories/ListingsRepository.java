package com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;

import java.util.Optional;

public interface ListingsRepository {

    void addNew(Listing listing);

    Optional<Listing> findById(ListingId id);
}
