package com.azat4dev.demobooking.users.users_commands.data.entities;

import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;


public record UserData(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String email,
    String encodedPassword,
    String firstName,
    String lastName,
    EmailVerificationStatus emailVerificationStatus
) {

    public static UserData makeFrom(NewUserData newUserData, LocalDateTime updatedAt) {
        return new UserData(
            newUserData.userId().value(),
            newUserData.createdAt(),
            updatedAt,
            newUserData.email().getValue(),
            newUserData.encodedPassword().value(),
            newUserData.fullName().getFirstName().getValue(),
            newUserData.fullName().getLastName().getValue(),
            newUserData.emailVerificationStatus()
        );
    }

    public static UserData makeFrom(User user, LocalDateTime updatedAt) {
        return new UserData(
            user.id().value(),
            user.createdAt(),
            updatedAt,
            user.email().getValue(),
            user.encodedPassword().value(),
            user.fullName().getFirstName().getValue(),
            user.fullName().getLastName().getValue(),
            user.emailVerificationStatus()
        );
    }
}
