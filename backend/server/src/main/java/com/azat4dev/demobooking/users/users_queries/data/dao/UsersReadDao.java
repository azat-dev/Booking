package com.azat4dev.demobooking.users.users_queries.data.dao;

import com.azat4dev.demobooking.users.users_queries.data.dao.entities.PersonalUserInfo;

import java.util.Optional;
import java.util.UUID;

public interface UsersReadDao {

    Optional<PersonalUserInfo> getPersonalUserInfoById(UUID userId);
}
