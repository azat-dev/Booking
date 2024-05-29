package com.azat4dev.booking.users.users_commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;


public interface GenerateUserPhotoObjectName {
    MediaObjectName execute(UserId userId, PhotoFileExtension extension);
}
