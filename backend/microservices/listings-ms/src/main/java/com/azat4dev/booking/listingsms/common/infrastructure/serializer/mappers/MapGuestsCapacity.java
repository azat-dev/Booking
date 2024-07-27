package com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.generated.events.dto.GuestsCapacityDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapGuestsCapacity implements Serializer<GuestsCapacity, GuestsCapacityDTO> {
    @Override
    public GuestsCapacityDTO serialize(GuestsCapacity dm) {
        return GuestsCapacityDTO.builder()
            .adults(dm.getAdults())
            .children(dm.getChildren())
            .infants(dm.getInfants())
            .build();
    }

    @Override
    public GuestsCapacity deserialize(GuestsCapacityDTO dto) {
        return GuestsCapacity.dangerouslyMake(
            dto.getAdults(),
            dto.getChildren(),
            dto.getInfants()
        );
    }

    @Override
    public Class<GuestsCapacity> getOriginalClass() {
        return GuestsCapacity.class;
    }

    @Override
    public Class<GuestsCapacityDTO> getSerializedClass() {
        return GuestsCapacityDTO.class;
    }
}