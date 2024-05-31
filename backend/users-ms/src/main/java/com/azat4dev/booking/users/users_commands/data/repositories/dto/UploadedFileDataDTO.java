package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;

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
