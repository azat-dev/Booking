package com.azat4dev.booking.users.commands.infrastructure.entities;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;

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
