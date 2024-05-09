package com.azat4dev.demobooking.users.users_queries.data.dao;

import com.azat4dev.demobooking.users.users_queries.data.dao.records.UserRecord;

import java.util.Optional;
import java.util.UUID;

public interface UsersReadDao {

    Optional<UserRecord> getById(UUID userId);
}
