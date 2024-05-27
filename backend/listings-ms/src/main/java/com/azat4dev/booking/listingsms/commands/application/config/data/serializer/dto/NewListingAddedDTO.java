package com.azat4dev.booking.listingsms.commands.application.config.data.serializer.dto;

import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.data.DomainEventPayloadDTO;
import com.azat4dev.booking.shared.domain.event.DomainEventPayload;

import java.util.UUID;

public record NewListingAddedDTO(
    UUID listingId,
    UUID ownerId,
    String title
) implements DomainEventPayloadDTO {

    public static NewListingAddedDTO fromDomain(NewListingAdded dm) {
        return new NewListingAddedDTO(
            dm.listingId().getValue(),
            dm.ownerId().getValue(),
            dm.title().getValue()
        );
    }

    @Override
    public DomainEventPayload toDomain() {
        return new NewListingAdded(
            ListingId.dangerouslyMakeFrom(listingId.toString()),
            OwnerId.dangerouslyMakeFrom(ownerId.toString()),
            ListingTitle.dangerouslyMakeFrom(title)
        );
    }
}
