package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.users.commands.domain.core.values.user.FirstName;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;
import com.azat4dev.booking.users.commands.domain.core.values.user.LastName;
import com.azat4dev.booking.shared.data.serializers.Serializer;
import com.azat4dev.booking.usersms.generated.events.dto.FullNameDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapFullName implements Serializer<FullName, FullNameDTO> {

    @Override
    public FullNameDTO serialize(FullName dm) {
        return FullNameDTO.builder()
            .firstName(dm.getFirstName().getValue())
            .lastName(dm.getLastName().getValue())
            .build();
    }

    @Override
    public FullName deserialize(FullNameDTO dto) {
        return FullName.makeWithoutChecks(
            FirstName.dangerMakeFromStringWithoutCheck(dto.getFirstName()),
            LastName.dangerMakeFromStringWithoutCheck(dto.getLastName())
        );
    }

    @Override
    public Class<FullName> getOriginalClass() {
        return FullName.class;
    }

    @Override
    public Class<FullNameDTO> getSerializedClass() {
        return FullNameDTO.class;
    }
}
