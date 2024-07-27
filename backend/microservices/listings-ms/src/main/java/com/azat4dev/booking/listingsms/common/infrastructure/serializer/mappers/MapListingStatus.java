package com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingStatusDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapListingStatus implements Serializer<ListingStatus, ListingStatusDTO> {
    @Override
    public ListingStatusDTO serialize(ListingStatus dm) {
        return ListingStatusDTO.fromValue(dm.name());
    }

    @Override
    public ListingStatus deserialize(ListingStatusDTO dto) {
        return ListingStatus.valueOf(dto.getValue());
    }

    @Override
    public Class<ListingStatus> getOriginalClass() {
        return ListingStatus.class;
    }

    @Override
    public Class<ListingStatusDTO> getSerializedClass() {
        return ListingStatusDTO.class;
    }
}