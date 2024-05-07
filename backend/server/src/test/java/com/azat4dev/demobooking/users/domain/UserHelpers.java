package com.azat4dev.demobooking.users.domain;

import com.azat4dev.demobooking.users.domain.entities.FirstName;
import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.entities.LastName;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserHelpers {
    public static final Faker faker = Faker.instance();

    public static EmailAddress anyValidEmail() {
        try {
            return EmailAddress.makeFromString(faker.internet().emailAddress());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FullName anyFullName() {
        try {
            return new FullName(
                new FirstName(faker.name().firstName()),
                new LastName(faker.name().lastName())
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
