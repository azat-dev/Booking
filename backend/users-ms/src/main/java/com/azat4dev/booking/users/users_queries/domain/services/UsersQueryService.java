package com.azat4dev.booking.users.users_queries.domain.services;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public interface UsersQueryService {

    Optional<PersonalUserInfo> getPersonalInfoById(UserId id);
}
