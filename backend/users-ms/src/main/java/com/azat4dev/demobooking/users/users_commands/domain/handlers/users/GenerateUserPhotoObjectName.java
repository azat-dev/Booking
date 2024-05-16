package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;


public interface GenerateUserPhotoObjectName {
    MediaObjectName execute(UserId userId);
}
