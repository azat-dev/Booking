package com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.generated.events.dto.RoomTypeDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapRoomType implements Serializer<RoomType, RoomTypeDTO> {
    @Override
    public RoomTypeDTO serialize(RoomType dm) {
        return RoomTypeDTO.fromValue(dm.name());
    }

    @Override
    public RoomType deserialize(RoomTypeDTO dto) {
        return RoomType.valueOf(dto.getValue());
    }

    @Override
    public Class<RoomType> getOriginalClass() {
        return RoomType.class;
    }

    @Override
    public Class<RoomTypeDTO> getSerializedClass() {
        return RoomTypeDTO.class;
    }
}