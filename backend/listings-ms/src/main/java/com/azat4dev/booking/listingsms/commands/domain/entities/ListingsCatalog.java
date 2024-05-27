package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;

import java.util.Optional;

public interface ListingsCatalog {

    void addNew(
        ListingId listingId,
        OwnerId ownerId,
        ListingTitle title
    );
}
