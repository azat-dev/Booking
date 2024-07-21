package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.events.FailedToAddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.generated.events.dto.FailedToAddNewListingDTO;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapFailedToAddNewListing implements MapPayload<FailedToAddNewListing, FailedToAddNewListingDTO> {

    @Override
    public FailedToAddNewListingDTO toDTO(FailedToAddNewListing dm) {
        return FailedToAddNewListingDTO.builder()
            .listingId(dm.listingId().getValue())
            .hostId(dm.hostId().getValue())
            .title(dm.title().getValue())
            .build();
    }

    @Override
    public FailedToAddNewListing toDomain(FailedToAddNewListingDTO dto) {
        return new FailedToAddNewListing(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            HostId.dangerouslyMakeFrom(dto.getHostId().toString()),
            ListingTitle.dangerouslyMakeFrom(dto.getTitle())
        );
    }

    @Override
    public Class<FailedToAddNewListing> getDomainClass() {
        return FailedToAddNewListing.class;
    }

    @Override
    public Class<FailedToAddNewListingDTO> getDTOClass() {
        return FailedToAddNewListingDTO.class;
    }
}