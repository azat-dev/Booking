package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

public record UserPhotoPathDTO(
    String bucketName,
    String objectName
) {

    public static UserPhotoPathDTO fromDomain(UserPhotoPath dm) {
        return new UserPhotoPathDTO(
            dm.bucketName().toString(),
            dm.objectName().toString()
        );
    }

    public UserPhotoPath toDomain() {
        return new UserPhotoPath(
            BucketName.makeWithoutChecks(bucketName),
            MediaObjectName.dangerouslyMake(objectName)
        );
    }
}
