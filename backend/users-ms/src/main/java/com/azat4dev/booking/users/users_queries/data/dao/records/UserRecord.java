package com.azat4dev.booking.users.users_queries.data.dao.records;

import java.util.Optional;
import java.util.UUID;

public record UserRecord(
    UUID id,
    String email,
    EmailVerificationStatus emailVerificationStatus,
    String firstName,
    String lastName,
    Optional<PhotoPath> photo
) {

    public record PhotoPath(
        String bucketName,
        String objectName
    ) {
    }

    public enum EmailVerificationStatus {
        NOT_VERIFIED,
        VERIFIED
    }
}
