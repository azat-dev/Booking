package com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;

public interface ListingsRepository {

    void addNew(Listing listing);
}
