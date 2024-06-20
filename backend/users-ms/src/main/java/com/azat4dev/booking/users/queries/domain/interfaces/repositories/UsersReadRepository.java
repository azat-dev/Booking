package com.azat4dev.booking.users.queries.domain.interfaces.repositories;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public interface UsersReadRepository {
    Optional<PersonalUserInfo> getPersonalUserInfoById(UserId id);
}

