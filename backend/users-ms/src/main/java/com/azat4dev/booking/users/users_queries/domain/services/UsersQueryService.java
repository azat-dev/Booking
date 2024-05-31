package com.azat4dev.booking.users.users_queries.domain.services;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public interface UsersQueryService {

    Optional<PersonalUserInfo> getPersonalInfoById(UserId id);
}
