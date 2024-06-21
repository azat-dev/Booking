package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.entities.User;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.commands.infrastructure.persistence.dao.UsersDao;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserDataToDomain;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserToData;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class UsersRepositoryImpl implements UsersRepository {

    private static final String METRICS_PREFIX = "users-ms.commands.infrastructure.persistence.users-repository.";

    private final MapUserToData mapUserToData;
    private final MapUserDataToDomain mapUserDataToDomain;
    private final UsersDao usersDao;

    @Counted(METRICS_PREFIX + "add-new.count")
    @Timed(METRICS_PREFIX + "add-new.time")
    @Override
    public void addNew(User user) throws Exception.UserWithSameEmailAlreadyExists {

        final var userData = this.mapUserToData.map(user);

        try {
            this.usersDao.addNew(userData);
        } catch (UsersDao.Exception.UserAlreadyExists e) {
            throw new Exception.UserWithSameEmailAlreadyExists();
        }
    }

    @Counted(METRICS_PREFIX + "update.count")
    @Timed(METRICS_PREFIX + "update.time")
    @Override
    public void update(User user) {

        final var userData = this.mapUserToData.map(user);

        try {
            this.usersDao.update(userData);
        } catch (UsersDao.Exception.UserNotFound e) {
            throw new Exception.UserNotFound(user.getId());
        }
    }

    @Counted(METRICS_PREFIX + "find-by-id.count")
    @Timed(METRICS_PREFIX + "find-by-id.time")
    @Override
    public Optional<User> findById(UserId id) {

        final var foundUserResult = this.usersDao.findById(id.value());
        return foundUserResult.map(userData -> {
            try {
                return this.mapUserDataToDomain.map(userData);
            } catch (DomainException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Counted(METRICS_PREFIX + "find-by-email.count")
    @Timed(METRICS_PREFIX + "find-by-email.time")
    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        final var foundUserResult = this.usersDao.findByEmail(email.getValue());
        return foundUserResult.map(userData -> {
            try {
                return this.mapUserDataToDomain.map(userData);
            } catch (DomainException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
