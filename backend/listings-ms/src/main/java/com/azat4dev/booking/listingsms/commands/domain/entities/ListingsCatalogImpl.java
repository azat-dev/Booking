package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListingsCatalogImpl implements ListingsCatalog {

    private final UnitOfWork unitOfWork;

    @Override
    public void addNew(
        ListingId listingId,
        OwnerId ownerId,
        ListingTitle title
    ) {
        final var newListing = new Listing(
            listingId,
            ownerId,
            title
        );
    }
}
