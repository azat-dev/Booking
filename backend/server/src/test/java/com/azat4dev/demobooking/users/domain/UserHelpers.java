package com.azat4dev.demobooking.users.domain;

import com.azat4dev.demobooking.users.domain.entities.FirstName;
import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.entities.LastName;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserHelpers {
    public static EmailAddress anyValidEmail() {
        try {
            return EmailAddress.makeFromString("user@examples.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FullName anyFullName() {
        try {
            return new FullName(
                new FirstName("John"),
                new LastName("Doe")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserId anyValidUserId() {
        try {
            return UserId.fromString(UUID.randomUUID().toString());
        } catch (UserId.WrongFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static EncodedPassword anyEncodedPassword() {
        return new EncodedPassword("encodedPassword");
    }

    public static User anyUser() {
        return new User(
            anyValidUserId(),
            LocalDateTime.now(),
            anyValidEmail(),
            anyFullName(),
            anyEncodedPassword(),
            EmailVerificationStatus.NOT_VERIFIED
        );
    }
}
