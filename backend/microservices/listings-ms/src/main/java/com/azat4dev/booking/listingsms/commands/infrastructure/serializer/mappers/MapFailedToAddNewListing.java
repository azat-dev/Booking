package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.events.FailedToAddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.generated.events.dto.FailedToAddNewListingDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapFailedToAddNewListing implements MapDomainEvent<FailedToAddNewListing, FailedToAddNewListingDTO> {

    @Override
    public FailedToAddNewListingDTO serialize(FailedToAddNewListing dm) {
        return FailedToAddNewListingDTO.builder()
            .listingId(dm.listingId().getValue())
            .hostId(dm.hostId().getValue())
            .title(dm.title().getValue())
            .build();
    }

    @Override
    public FailedToAddNewListing deserialize(FailedToAddNewListingDTO dto) {
        return new FailedToAddNewListing(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            HostId.dangerouslyMakeFrom(dto.getHostId().toString()),
            ListingTitle.dangerouslyMakeFrom(dto.getTitle())
        );
    }

    @Override
    public Class<FailedToAddNewListing> getOriginalClass() {
        return FailedToAddNewListing.class;
    }

    @Override
    public Class<FailedToAddNewListingDTO> getSerializedClass() {
        return FailedToAddNewListingDTO.class;
    }
}