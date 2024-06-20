package com.azat4dev.booking.users.queries.infrastructure.persistence.dao;

import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.records.UserRecord;

import java.util.Optional;
import java.util.UUID;

public interface UsersReadDao {

    Optional<UserRecord> getById(UUID userId);
}
