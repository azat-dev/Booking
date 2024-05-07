package com.azat4dev.demobooking.users.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.time.LocalDateTime;

public record NewUserData(
    UserId userId,
    LocalDateTime createdAt,
    EmailAddress email,
    FullName fullName,
    EncodedPassword encodedPassword,
    EmailVerificationStatus emailVerificationStatus
) {
}