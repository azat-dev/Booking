package com.azat4dev.booking.users.commands.domain;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.entities.User;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.commands.domain.core.values.user.FirstName;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;
import com.azat4dev.booking.users.commands.domain.core.values.user.LastName;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class UserHelpers {
    public static final Faker faker = new Faker();

    public static EmailAddress anyValidEmail() {
        try {
            return EmailAddress.checkAndMakeFromString(faker.internet().emailAddress());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FullName anyFullName() {
        try {
            return FullName.makeWithChecks(
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
