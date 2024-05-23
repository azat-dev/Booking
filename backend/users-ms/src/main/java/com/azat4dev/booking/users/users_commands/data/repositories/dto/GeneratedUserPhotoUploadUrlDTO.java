package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.files.UploadFileFormData;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

public record GeneratedUserPhotoUploadUrlDTO(
    String userId,
    String url,
    String bucketName,
    String objectName,
    Map<String, String> formData
) implements DomainEventPayloadDTO {

    public static GeneratedUserPhotoUploadUrlDTO fromDomain(GeneratedUserPhotoUploadUrl event) {
        return new GeneratedUserPhotoUploadUrlDTO(
            event.userId().toString(),
            event.formData().url().toString(),
            event.formData().bucketName().toString(),
            event.formData().objectName().toString(),
            event.formData().value()
        );
    }

    public GeneratedUserPhotoUploadUrl toDomain() {

        try {
            return new GeneratedUserPhotoUploadUrl(
                UserId.dangerouslyMakeFrom(userId),
                new UploadFileFormData(
                    URI.create(url).toURL(),
                    BucketName.makeWithoutChecks(bucketName),
                    MediaObjectName.dangerouslyMake(objectName),
                    formData
                )
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
