package com.azat4dev.demobooking.users.users_queries.domain.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public interface UsersQueryService {

    Optional<PersonalUserInfo> getById(UserId id);
}
