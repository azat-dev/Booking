package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.data.repositories.jpa.JpaUsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserId;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public class UsersRepositoryImpl implements UsersRepository {

    private final MapNewUserToData mapNewUserToData;
    private final MapUserDataToDomain mapUserDataToDomain;
    private final JpaUsersRepository jpaUsersRepository;

    public UsersRepositoryImpl(
        MapNewUserToData mapNewUserToData,
        MapUserDataToDomain mapUserDataToDomain,
        JpaUsersRepository jpaUsersRepository
    ) {
        this.mapUserDataToDomain = mapUserDataToDomain;
        this.mapNewUserToData = mapNewUserToData;
        this.jpaUsersRepository = jpaUsersRepository;
    }

    @Override
    public void createUser(NewUserData newUserData) throws UserWithSameEmailAlreadyExistsException {

        final var userData = this.mapNewUserToData.map(newUserData);

        try {
            this.jpaUsersRepository.saveAndFlush(userData);
        } catch (RuntimeException e) {

            final var email = newUserData.email().getValue();

            final var foundUserResult = this.jpaUsersRepository.findByEmail(email);
            foundUserResult.orElseThrow(() -> e);

            throw new UserWithSameEmailAlreadyExistsException();
        }
    }

    @Override
    public Optional<User> findById(UserId id) {

        try {
            final var foundUser = this.jpaUsersRepository.getReferenceById(id.value());
            if (foundUser == null) {
                return Optional.empty();
            }

            try {
                return Optional.of(this.mapUserDataToDomain.map(foundUser));
            } catch (DomainException e) {
                throw new RuntimeException(e);
            }
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        final var foundUserResult = this.jpaUsersRepository.findByEmail(email.getValue());
        return foundUserResult.map(userData -> {
            try {
                return this.mapUserDataToDomain.map(userData);
            } catch (DomainException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
