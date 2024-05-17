package com.azat4dev.demobooking.users.users_commands.data.jpa;


import com.azat4dev.demobooking.users.users_commands.application.config.DaoConfig;
import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.data.repositories.dao.UsersDao;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@PostgresTest
@Import(DaoConfig.class)
public class UsersDaoJdbcTests {

    @Autowired
    private UsersDao dao;

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    void test_findByEmail_givenEmptyDb_thenReturnEmpty() {

        // Given
        final var email = UserHelpers.anyValidEmail().getValue();

        // When
        final var result = dao.findByEmail(email);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    @Sql("/db/data.sql")
    void test_findByEmail_givenValidEmail_thenReturnUser() {

        // Given
        final var existingUser1 = givenExistingUser();
        final var existingUser2 = givenExistingUser();

        // When
        final var result1 = dao.findByEmail(existingUser1.email());
        final var result2 = dao.findByEmail(existingUser2.email());

        // Then
        assertThat(result1.get()).isEqualTo(existingUser1);
        assertThat(result2.get()).isEqualTo(existingUser2);
    }

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    @Sql("/db/data.sql")
    void test_addNew_givenExistingUser_thenThrowException() {

        // Given
        final var existingUser = givenExistingUser();

        // When
        final var exception = assertThrows(
            UsersDao.Exception.UserAlreadyExists.class,
            () -> dao.addNew(existingUser)
        );

        // Then
        assertThat(exception).isNotNull();
    }

    UserData anyUserData() {
        final var user = UserHelpers.anyUser();
        return new UserData(
            user.getId().value(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            user.getEmail().getValue(),
            user.getFullName().getFirstName().getValue(),
            user.getFullName().getLastName().getValue(),
            user.getEncodedPassword().value(),
            EmailVerificationStatus.NOT_VERIFIED
        );
    }

    UserData givenExistingUser() {

        final var userData = anyUserData();
        dao.addNew(userData);
        return userData;
    }

    @Test
    @Sql({"/db/drop-schema.sql", "/db/schema.sql", "/db/data.sql"})
    void test_update_givenExistingUserId_thenReturn() {

        // Given
        final var existingData = givenExistingUser();

        final var expectedUser = new UserData(
            existingData.id(),
            existingData.createdAt(),
            LocalDateTime.now(),
            "new@email.com",
            "updatedpassword",
            "updatedFirstName",
            "updatedLastName",
            EmailVerificationStatus.VERIFIED
        );
        final var existingUserId = expectedUser.id();

        // When
        dao.update(expectedUser);
        final var result = dao.findById(existingUserId);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    @Test
    @Sql({"/db/drop-schema.sql", "/db/schema.sql", "/db/data.sql"})
    void test_update_givenNotExistingUserId_thenReturn() {

        // Given
        final var existingData = givenExistingUser();

        final var expectedUser = new UserData(
            UserHelpers.anyValidUserId().value(),
            existingData.createdAt(),
            LocalDateTime.now(),
            "new@email.com",
            "updatedpassword",
            "updatedFirstName",
            "updatedLastName",
            EmailVerificationStatus.VERIFIED
        );

        final var existingUserId = expectedUser.id();

        // When
        final var exception = assertThrows(UsersDao.Exception.UserNotFound.class, () -> dao.update(expectedUser));

        // Then
        assertThat(exception).isInstanceOf(UsersDao.Exception.UserNotFound.class);
    }
}
