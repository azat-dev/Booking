package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;

import java.util.Optional;

public record PrivateListingDetails(
    ListingId id,
    ListingTitle title,
    ListingStatus status,
    Optional<ListingDescription> description
) {
}
