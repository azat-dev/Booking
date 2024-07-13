package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.entities.User;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.commands.infrastructure.persistence.dao.UsersDao;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserDataToDomain;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserToData;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Observed
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {

    private final MapUserToData mapUserToData;
    private final MapUserDataToDomain mapUserDataToDomain;
    private final UsersDao usersDao;

    @Override
    public void addNew(User user) throws Exception.UserWithSameEmailAlreadyExists {

        final var userData = this.mapUserToData.map(user);

        try {
            this.usersDao.addNew(userData);

            log.atInfo()
                .addKeyValue("userId", user::getId)
                .addArgument(user::getId)
                .log("User added: {}");

        } catch (UsersDao.Exception.UserAlreadyExists e) {
            log.atWarn()
                .addKeyValue("userId", user::getId)
                .log("User already exists");
            throw new Exception.UserWithSameEmailAlreadyExists();
        }
    }

    @Override
    public void update(User user) {

        final var userData = this.mapUserToData.map(user);

        try {
            this.usersDao.update(userData);

            log.atInfo()
                .addKeyValue("userId", user::getId)
                .log("User updated");
        } catch (UsersDao.Exception.UserNotFound e) {
            log.atDebug()
                .setCause(e)
                .addKeyValue("userId", user::getId)
                .log("User not found");
            throw new Exception.UserNotFound(user.getId());
        }
    }

    @Override
    public Optional<User> findById(UserId id) {

        final var foundUserResult = this.usersDao.findById(id.value());
        return foundUserResult.map(userData -> {
            try {
                return this.mapUserDataToDomain.map(userData);
            } catch (DomainException e) {
                log.atDebug()
                    .addKeyValue("userId", id)
                    .setCause(e)
                    .log("User not found");
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        final var foundUserResult = this.usersDao.findByEmail(email.getValue());
        return foundUserResult.map(userData -> {
            try {
                return this.mapUserDataToDomain.map(userData);
            } catch (DomainException e) {
                log.atWarn().log("User not found");
                throw new RuntimeException(e);
            }
        });
    }
}
