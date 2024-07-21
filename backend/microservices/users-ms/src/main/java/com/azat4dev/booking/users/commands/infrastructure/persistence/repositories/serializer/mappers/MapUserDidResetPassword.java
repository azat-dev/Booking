package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.usersms.generated.events.dto.UserDidResetPasswordDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapUserDidResetPassword implements MapPayload<UserDidResetPassword, UserDidResetPasswordDTO> {

    @Override
    public UserDidResetPasswordDTO toDTO(UserDidResetPassword dm) {
        return UserDidResetPasswordDTO.builder()
            .userId(dm.userId().toString())
            .build();
    }

    @Override
    public UserDidResetPassword toDomain(UserDidResetPasswordDTO dto) {
        return new UserDidResetPassword(
            UserId.dangerouslyMakeFrom(dto.getUserId())
        );
    }

    @Override
    public Class<UserDidResetPassword> getDomainClass() {
        return UserDidResetPassword.class;
    }

    @Override
    public Class<UserDidResetPasswordDTO> getDTOClass() {
        return UserDidResetPasswordDTO.class;
    }
}