package com.azat4dev.booking.users.users_queries.domain.services;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;
import com.azat4dev.booking.users.users_queries.domain.interfaces.repositories.UsersReadRepository;

import java.util.Optional;

public final class UsersQueryServiceImpl implements UsersQueryService {

    private final UsersReadRepository usersReadRepository;

    public UsersQueryServiceImpl(
        UsersReadRepository usersReadRepository
    ) {
        this.usersReadRepository = usersReadRepository;
    }

    @Override
    public Optional<PersonalUserInfo> getPersonalInfoById(UserId id) {
        return usersReadRepository.getPersonalUserInfoById(id);
    }
}