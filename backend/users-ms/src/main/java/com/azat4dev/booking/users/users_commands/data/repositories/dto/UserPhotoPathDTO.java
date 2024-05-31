package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;

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
        UserPhotoPath userPhotoPath = new UserPhotoPath(
            BucketName.makeWithoutChecks(bucketName),
            MediaObjectName.dangerouslyMake(objectName)
        );
        return userPhotoPath;
    }
}
