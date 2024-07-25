package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.generated.events.dto.NewListingAddedDTO;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapNewListingAdded implements MapDomainEvent<NewListingAdded, NewListingAddedDTO> {

    @Override
    public NewListingAddedDTO serialize(NewListingAdded dm) {
        return NewListingAddedDTO.builder()
            .listingId(dm.listingId().getValue())
            .hostId(dm.hostId().getValue())
            .title(dm.title().getValue())
            .build();
    }

    @Override
    public NewListingAdded deserialize(NewListingAddedDTO dto) {
        return new NewListingAdded(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            HostId.dangerouslyMakeFrom(dto.getHostId().toString()),
            ListingTitle.dangerouslyMakeFrom(dto.getTitle())
        );
    }

    @Override
    public Class<NewListingAdded> getOriginalClass() {
        return NewListingAdded.class;
    }

    @Override
    public Class<NewListingAddedDTO> getSerializedClass() {
        return NewListingAddedDTO.class;
    }
}