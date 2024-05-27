package com.azat4dev.booking.listingsms.commands.application.config.data.serializer.dto;

import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;

import java.util.UUID;

public record NewListingAddedDTO(
    UUID listingId,
    UUID ownerId,
    String title
) {

    public static NewListingAddedDTO from(NewListingAdded dm) {
        return new NewListingAddedDTO(
            dm.listingId().getValue(),
            dm.ownerId().getValue(),
            dm.title().getValue()
        );
    }
}
