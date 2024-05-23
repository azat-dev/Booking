package com.azat4dev.booking.users.users_commands.data.entities;

import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public record UserData(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String email,
    String encodedPassword,
    String firstName,
    String lastName,
    EmailVerificationStatus emailVerificationStatus,
    Optional<PhotoPath> photo
) {
    public record PhotoPath(
        String bucketName,
        String objectName
    ) {

        public static PhotoPath fromDomain(UserPhotoPath path) {
            return new PhotoPath(
                path.bucketName().toString(),
                path.objectName().toString()
            );
        }

        public UserPhotoPath toDomain() {
            return new UserPhotoPath(
                BucketName.makeWithoutChecks(bucketName),
                MediaObjectName.dangerouslyMake(objectName)
            );
        }
    }
}
