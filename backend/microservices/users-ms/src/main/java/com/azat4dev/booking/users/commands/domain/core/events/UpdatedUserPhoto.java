package com.azat4dev.booking.users.commands.domain.core.events;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;

import java.util.Optional;

public record UpdatedUserPhoto(
    UserId userId,
    UserPhotoPath newPhotoPath,
    Optional<UserPhotoPath> prevPhotoPath
) implements DomainEventPayload, EventWithUserId {
}
