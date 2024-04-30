package com.azat4dev.demobooking.users.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import org.springframework.lang.NonNull;

import java.util.Date;

public record NewUserData(
    UserId userId,
    Date createdAt,
    EmailAddress email,
    EncodedPassword encodedPassword,
    EmailVerificationStatus emailVerificationStatus
) {
}