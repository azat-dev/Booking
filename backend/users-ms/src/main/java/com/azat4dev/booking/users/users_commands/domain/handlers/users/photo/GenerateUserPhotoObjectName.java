package com.azat4dev.booking.users.users_commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;


public interface GenerateUserPhotoObjectName {
    MediaObjectName execute(UserId userId, PhotoFileExtension extension);
}
