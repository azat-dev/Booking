package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;


import com.azat4dev.booking.listingsms.commands.domain.events.ListingDetailsUpdated;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingDetailsFieldsDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingDetailsUpdatedDTO;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.shared.data.serializers.Mapper;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public final class MapListingDetailsUpdated implements MapPayload<ListingDetailsUpdated, ListingDetailsUpdatedDTO> {

    private final Mapper<ListingDetailsUpdated.Change, ListingDetailsFieldsDTO> mapListingChange;
    private final Mapper<LocalDateTime, String> mapLocalDateTime;

    @Override
    public ListingDetailsUpdatedDTO toDTO(ListingDetailsUpdated dm) {
        return ListingDetailsUpdatedDTO.builder()
            .updatedAt(mapLocalDateTime.toDTO(dm.updatedAt()))
            .listingId(dm.listingId().getValue())
            .newValues(mapListingChange.toDTO(dm.newState()))
            .prevValues(mapListingChange.toDTO(dm.previousState()))
            .build();
    }

    @Override
    public ListingDetailsUpdated toDomain(ListingDetailsUpdatedDTO dto) {
        return new ListingDetailsUpdated(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            mapLocalDateTime.toDomain(dto.getUpdatedAt()),
            mapListingChange.toDomain(dto.getPrevValues()),
            mapListingChange.toDomain(dto.getNewValues())
        );
    }

    @Override
    public Class<ListingDetailsUpdated> getDomainClass() {
        return ListingDetailsUpdated.class;
    }

    @Override
    public Class<ListingDetailsUpdatedDTO> getDTOClass() {
        return ListingDetailsUpdatedDTO.class;
    }
}