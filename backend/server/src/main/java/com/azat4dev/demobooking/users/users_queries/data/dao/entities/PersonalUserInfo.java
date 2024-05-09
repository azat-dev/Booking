package com.azat4dev.demobooking.users.users_queries.data.dao.entities;

import java.util.UUID;

public record PersonalUserInfo(
    UUID id,
    String email,
    String firstName,
    String lastName
) {
}
