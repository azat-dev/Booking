package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserDidResetPassword;

public record UserDidResetPasswordDTO(String userId) implements DomainEventPayloadDTO {

    public static UserDidResetPasswordDTO fromDomain(UserDidResetPassword event) {
        return new UserDidResetPasswordDTO(event.userId().toString());
    }

    public UserDidResetPassword toDomain() {
        return new UserDidResetPassword(UserId.dangerouslyMakeFrom(userId));
    }
}
