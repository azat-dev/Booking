package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.users.users_commands.domain.core.values.files.UploadedFileData;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

public record UploadedFileDataDTO(
    String bucketName,
    String objectName
) {

    public static UploadedFileDataDTO fromDomain(UploadedFileData dm) {
        return new UploadedFileDataDTO(
            dm.bucketName().toString(),
            dm.objectName().toString()
        );
    }

    public UploadedFileData toDomain() {
        return new UploadedFileData(
            BucketName.makeWithoutChecks(bucketName),
            MediaObjectName.dangerouslyMake(objectName)
        );
    }
}
