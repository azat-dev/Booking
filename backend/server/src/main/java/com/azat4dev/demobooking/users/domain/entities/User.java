package com.azat4dev.demobooking.users.domain.entities;

import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import org.springframework.lang.NonNull;

import java.util.Date;

public record User(
    UserId id,
    Date createdAt,
    EmailAddress email,
    FullName fullName,
    EncodedPassword encodedPassword,
    EmailVerificationStatus emailVerificationStatus
) {
}
