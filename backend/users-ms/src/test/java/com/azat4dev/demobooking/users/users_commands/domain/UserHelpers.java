package com.azat4dev.demobooking.users.users_commands.domain;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.LastName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class UserHelpers {
    public static final Faker faker = Faker.instance();

    public static EmailAddress anyValidEmail() {
        try {
            return EmailAddress.checkAndMakeFromString(faker.internet().emailAddress());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FullName anyFullName() {
        try {
            return new FullName(
                FirstName.checkAndMakeFromString(faker.name().firstName()),
                LastName.dangerMakeFromStringWithoutCheck(faker.name().lastName())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserId anyValidUserId() {
        try {
            return UserId.fromUUID(UUID.randomUUID());
        } catch (UserId.WrongFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static EncodedPassword anyEncodedPassword() {
        return new EncodedPassword("encodedPassword");
    }

    public static User anyUser() {
        return User.dangerouslyMakeFrom(
            anyValidUserId(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            anyValidEmail(),
            anyFullName(),
            anyEncodedPassword(),
            EmailVerificationStatus.NOT_VERIFIED,
            Optional.of(
                new UserPhotoPath(
                    BucketName.makeWithoutChecks("bucketname"),
                    MediaObjectName.dangerouslyMake("objectname")
                )
            )
        );
    }
}
