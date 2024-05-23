package com.azat4dev.bookingdemo.listingsms.commands.core.domain.interfaces.repositories;

import com.azat4dev.bookingdemo.listingsms.commands.core.domain.entities.Listing;

public interface ListingsRepository {

    void addNew(Listing listing);
}
