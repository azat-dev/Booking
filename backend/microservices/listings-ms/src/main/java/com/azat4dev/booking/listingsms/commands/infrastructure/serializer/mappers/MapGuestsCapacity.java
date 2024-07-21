package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.generated.events.dto.GuestsCapacityDTO;
import com.azat4dev.booking.shared.data.serializers.Mapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapGuestsCapacity implements Mapper<GuestsCapacity, GuestsCapacityDTO> {
    @Override
    public GuestsCapacityDTO toDTO(GuestsCapacity dm) {
        return GuestsCapacityDTO.builder()
            .adults(dm.getAdults())
            .children(dm.getChildren())
            .infants(dm.getInfants())
            .build();
    }

    @Override
    public GuestsCapacity toDomain(GuestsCapacityDTO dto) {
        return GuestsCapacity.dangerouslyMake(
            dto.getAdults(),
            dto.getChildren(),
            dto.getInfants()
        );
    }

    @Override
    public Class<GuestsCapacity> getDomainClass() {
        return GuestsCapacity.class;
    }

    @Override
    public Class<GuestsCapacityDTO> getDTOClass() {
        return GuestsCapacityDTO.class;
    }
}