package com.azat4dev.demobooking.users.users_queries.data.dao;

import com.azat4dev.demobooking.users.users_queries.data.dao.entities.PersonalUserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@Sql({"classpath:users/h2/drop-schema.sql", "classpath:users/h2/schema.sql", "classpath:users/h2/data.sql"})
public class UsersReadDaoJdbcTests {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    SUT createSUT() {
        return new SUT(new UsersReadDaoJdbc(jdbcTemplate));
    }

    PersonalUserInfo user1() {
        return new PersonalUserInfo(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "john@example.com",
            "John",
            "Doe"
        );
    }

    @Test
    void test_getPersonalUserInfoById_givenNotExistingUserId_thenReturnPersonalUserInfo() {

        // Given
        final var sut = createSUT();
        final var notExistingUserId = UUID.randomUUID();

        // When
        final var result = sut.dao.getPersonalUserInfoById(notExistingUserId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void test_getPersonalUserInfoById_givenExistingUserId_thenReturnPersonalUserInfo() {

        // Given
        final var sut = createSUT();
        final var expectedUser = user1();
        final var existingUserId = expectedUser.id();

        // When
        final var result = sut.dao.getPersonalUserInfoById(existingUserId);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    record SUT(UsersReadDao dao) {
    }

}
