package com.azat4dev.booking.users.queries.domain.entities;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;

public record PublicUserInfo(
    UserId id,
    String email,
    FullName fullName,
    UserPhoto photo
) {
}
