package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;

public interface ListingsCatalog {

    void addNew(
        ListingId listingId,
        OwnerId ownerId,
        ListingTitle title
    );
}
