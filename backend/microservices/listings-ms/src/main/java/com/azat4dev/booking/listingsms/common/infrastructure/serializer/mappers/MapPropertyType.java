package com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.PropertyTypeDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapPropertyType implements Serializer<PropertyType, PropertyTypeDTO> {
    @Override
    public PropertyTypeDTO serialize(PropertyType dm) {
        return PropertyTypeDTO.fromValue(dm.name());
    }

    @Override
    public PropertyType deserialize(PropertyTypeDTO dto) {
        return PropertyType.valueOf(dto.getValue());
    }

    @Override
    public Class<PropertyType> getOriginalClass() {
        return PropertyType.class;
    }

    @Override
    public Class<PropertyTypeDTO> getSerializedClass() {
        return PropertyTypeDTO.class;
    }
}