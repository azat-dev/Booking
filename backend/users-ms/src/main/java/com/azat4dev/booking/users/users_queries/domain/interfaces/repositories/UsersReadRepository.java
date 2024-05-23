package com.azat4dev.booking.users.users_queries.domain.interfaces.repositories;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public interface UsersReadRepository {
    Optional<PersonalUserInfo> getPersonalUserInfoById(UserId id);
}

