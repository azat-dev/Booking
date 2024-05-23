package com.azat4dev.booking.users.users_queries.domain.entities;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.FullName;

import java.util.Optional;


public record PersonalUserInfo(
    UserId id,
    String email,
    FullName fullName,
    Optional<UserPhoto> photo
) {
}
