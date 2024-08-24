package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.UserDidResetPasswordDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapUserDidResetPassword implements MapDomainEvent<UserDidResetPassword, UserDidResetPasswordDTO> {

    @Override
    public UserDidResetPasswordDTO serialize(UserDidResetPassword dm) {
        return UserDidResetPasswordDTO.builder()
            .userId(dm.userId().toString())
            .build();
    }

    @Override
    public UserDidResetPassword deserialize(UserDidResetPasswordDTO dto) {
        return new UserDidResetPassword(
            UserId.dangerouslyMakeFrom(dto.getUserId())
        );
    }

    @Override
    public Class<UserDidResetPassword> getOriginalClass() {
        return UserDidResetPassword.class;
    }

    @Override
    public Class<UserDidResetPasswordDTO> getSerializedClass() {
        return UserDidResetPasswordDTO.class;
    }
}