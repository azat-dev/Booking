package com.azat4dev.booking.users.commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;


public interface GenerateUserPhotoObjectName {
    MediaObjectName execute(UserId userId, PhotoFileExtension extension);
}
