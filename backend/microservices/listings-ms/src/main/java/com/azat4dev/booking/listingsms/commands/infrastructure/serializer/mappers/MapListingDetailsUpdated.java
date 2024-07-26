package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;


import com.azat4dev.booking.listingsms.commands.domain.events.ListingDetailsUpdated;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingDetailsFieldsDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingDetailsUpdatedDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public final class MapListingDetailsUpdated implements MapDomainEvent<ListingDetailsUpdated, ListingDetailsUpdatedDTO> {

    private final Serializer<ListingDetailsUpdated.Change, ListingDetailsFieldsDTO> mapListingChange;
    private final Serializer<LocalDateTime, String> mapLocalDateTime;

    @Override
    public ListingDetailsUpdatedDTO serialize(ListingDetailsUpdated dm) {
        return ListingDetailsUpdatedDTO.builder()
            .updatedAt(mapLocalDateTime.serialize(dm.updatedAt()))
            .listingId(dm.listingId().getValue())
            .newValues(mapListingChange.serialize(dm.newState()))
            .prevValues(mapListingChange.serialize(dm.previousState()))
            .build();
    }

    @Override
    public ListingDetailsUpdated deserialize(ListingDetailsUpdatedDTO dto) {
        return new ListingDetailsUpdated(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            mapLocalDateTime.deserialize(dto.getUpdatedAt()),
            mapListingChange.deserialize(dto.getPrevValues()),
            mapListingChange.deserialize(dto.getNewValues())
        );
    }

    @Override
    public Class<ListingDetailsUpdated> getOriginalClass() {
        return ListingDetailsUpdated.class;
    }

    @Override
    public Class<ListingDetailsUpdatedDTO> getSerializedClass() {
        return ListingDetailsUpdatedDTO.class;
    }
}