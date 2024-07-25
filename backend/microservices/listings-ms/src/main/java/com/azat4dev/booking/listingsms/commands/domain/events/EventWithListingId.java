package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;

public interface EventWithListingId {

    ListingId listingId();
}
