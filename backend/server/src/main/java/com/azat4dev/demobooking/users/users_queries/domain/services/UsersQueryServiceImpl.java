package com.azat4dev.demobooking.users.users_queries.domain.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_queries.domain.entities.User;
import com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories.UsersReadRepository;

import java.util.Optional;

public final class UsersQueryServiceImpl implements UsersQueryService {

    private final UsersReadRepository usersReadRepository;

    UsersQueryServiceImpl(
        UsersReadRepository usersReadRepository
    ) {
        this.usersReadRepository = usersReadRepository;
    }

    @Override
    public Optional<User> getById(UserId id) {
        return usersReadRepository.getById(id);
    }
}