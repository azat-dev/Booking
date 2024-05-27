package com.azat4dev.booking.listingsms.queries.presentation.api.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListingDetails;

public record ListingPrivateDetailsDTO(
    String id,
    String title,
    String description
) {

    public static ListingPrivateDetailsDTO fromDomain(PrivateListingDetails dm) {
        return new ListingPrivateDetailsDTO(
            dm.id().getValue().toString(),
            dm.title().getValue(),
            dm.description().map(ListingDescription::getValue).orElse(null)
        );
    }
}
