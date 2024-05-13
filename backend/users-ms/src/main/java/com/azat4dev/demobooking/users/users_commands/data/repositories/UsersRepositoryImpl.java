package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.data.repositories.dao.UsersDao;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class UsersRepositoryImpl implements UsersRepository {

    private final MapUserToData mapUserToData;
    private final MapUserDataToDomain mapUserDataToDomain;
    private final UsersDao usersDao;

    @Override
    public void addNew(User user) throws UserWithSameEmailAlreadyExistsException {

        final var userData = this.mapUserToData.map(user);

        try {
            this.usersDao.addNew(userData);
        } catch (UsersDao.UserAlreadyExistsException e) {
            throw new UserWithSameEmailAlreadyExistsException();
        }
    }

    @Override
    public void save(User user) {
        throw new RuntimeException("NOT IMPLEMENTED YET");
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
