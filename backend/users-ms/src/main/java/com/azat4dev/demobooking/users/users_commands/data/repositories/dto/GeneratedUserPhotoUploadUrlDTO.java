package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;

import java.net.MalformedURLException;
import java.net.URL;

public record GeneratedUserPhotoUploadUrlDTO(
    String userId,
    String url
) implements DomainEventPayloadDTO {

    public static GeneratedUserPhotoUploadUrlDTO fromDomain(GeneratedUserPhotoUploadUrl event) {
        return new GeneratedUserPhotoUploadUrlDTO(
            event.userId().toString(),
            event.url().toString()
        );
    }

    public GeneratedUserPhotoUploadUrl toDomain() {
        try {
            return new GeneratedUserPhotoUploadUrl(
                UserId.fromString(userId),
                new URL(url)
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
