package com.azat4dev.demobooking.users.users_queries.domain.entities;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FullName;

import java.util.Optional;


public record PersonalUserInfo(
    UserId id,
    String email,
    FullName fullName,
    Optional<UserPhoto> photo
) {
}
