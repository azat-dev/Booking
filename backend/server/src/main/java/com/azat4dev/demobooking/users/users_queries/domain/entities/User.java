package com.azat4dev.demobooking.users.users_queries.domain.entities;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

public record User(
    UserId id,
    String email
) {
}
