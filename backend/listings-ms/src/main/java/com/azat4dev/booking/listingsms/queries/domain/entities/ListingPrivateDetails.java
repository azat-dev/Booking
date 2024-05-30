package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;

import java.util.Optional;

public record ListingPrivateDetails(
    ListingId id,
    OwnerId ownerId,
    ListingTitle title,
    ListingStatus status,
    Optional<ListingDescription> description
) {
}
