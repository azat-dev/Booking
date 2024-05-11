package com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public interface UsersReadRepository {
    Optional<PersonalUserInfo> getPersonalUserInfoById(UserId id);
}

