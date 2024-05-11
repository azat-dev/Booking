package com.azat4dev.demobooking.users.users_queries.data.dao.records;

import java.util.UUID;

public record UserRecord(
    UUID id,
    String email,
    String firstName,
    String lastName
) {
}
