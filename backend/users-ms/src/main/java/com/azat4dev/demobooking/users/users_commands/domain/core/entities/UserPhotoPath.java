package com.azat4dev.demobooking.users.users_commands.domain.core.entities;

import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserPhotoPath {

    private final BucketName bucketName;
    private final MediaObjectName objectName;
}
