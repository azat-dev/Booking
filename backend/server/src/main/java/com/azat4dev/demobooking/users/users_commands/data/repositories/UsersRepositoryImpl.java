package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.data.repositories.dao.UsersDao;
import com.azat4dev.demobooking.users.users_commands.domain.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {

    private final MapNewUserToData mapNewUserToData;
    private final MapUserDataToDomain mapUserDataToDomain;
    private final UsersDao usersDao;

    @Override
    public void createUser(NewUserData newUserData) throws UserWithSameEmailAlreadyExistsException {

        final var userData = this.mapNewUserToData.map(newUserData);

        try {
            this.usersDao.addNew(userData);
        } catch (RuntimeException e) {

            final var email = newUserData.email().getValue();

            final var foundUserResult = this.usersDao.findByEmail(email);
            foundUserResult.orElseThrow(() -> e);

            throw new UserWithSameEmailAlreadyExistsException();
        }
    }

    @Override
    public Optional<User> findById(UserId id) {

        try {
            final var foundUser = this.usersDao.findById(id.value());
            return foundUser.map(this.mapUserDataToDomain::map);

        } catch (DomainException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        final var foundUserResult = this.usersDao.findByEmail(email.getValue());
        return foundUserResult.map(userData -> {
            try {
                return this.mapUserDataToDomain.map(userData);
            } catch (DomainException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
