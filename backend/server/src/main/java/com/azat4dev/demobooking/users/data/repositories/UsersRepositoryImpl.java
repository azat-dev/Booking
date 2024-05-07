package com.azat4dev.demobooking.users.data.repositories;

import com.azat4dev.demobooking.users.data.repositories.jpa.JpaUsersRepository;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.util.Optional;

public class UsersRepositoryImpl implements UsersRepository {

    private final MapNewUserToData mapNewUserToData;
    private final JpaUsersRepository jpaUsersRepository;

    public UsersRepositoryImpl(
        MapNewUserToData mapNewUserToData,
        JpaUsersRepository jpaUsersRepository
    ) {
        this.mapNewUserToData = mapNewUserToData;
        this.jpaUsersRepository = jpaUsersRepository;
    }

    @Override
    public void createUser(NewUserData newUserData) throws UserWithSameEmailAlreadyExistsException {

        final var userData = mapNewUserToData.map(newUserData);

        try {
            jpaUsersRepository.saveAndFlush(userData);
        } catch (RuntimeException e) {

            final var email = newUserData.email().getValue();

            final var foundUserResult = jpaUsersRepository.findByEmail(email);
            foundUserResult.orElseThrow(() -> e);

            throw new UserWithSameEmailAlreadyExistsException();
        }
    }

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        return Optional.empty();
    }
}
