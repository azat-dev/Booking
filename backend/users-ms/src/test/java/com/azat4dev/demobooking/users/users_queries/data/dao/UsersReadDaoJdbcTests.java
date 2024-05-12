package com.azat4dev.demobooking.users.users_queries.data.dao;

import com.azat4dev.demobooking.users.users_commands.data.jpa.PostgresTest;
import com.azat4dev.demobooking.users.users_queries.data.dao.records.UserRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@PostgresTest
public class UsersReadDaoJdbcTests {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    SUT createSUT() {
        return new SUT(new UsersReadDaoJdbc(jdbcTemplate));
    }

    UserRecord user1() {
        return new UserRecord(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "john@example.com",
            "John",
            "Doe"
        );
    }

    @Test
    @Sql({"/db/drop-schema.sql", "/db/schema.sql", "/db/data.sql"})
    void test_getById_givenNotExistingUserId_thenReturn() {

        // Given
        final var sut = createSUT();
        final var notExistingUserId = UUID.randomUUID();

        // When
        final var result = sut.dao.getById(notExistingUserId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @Sql({"/db/drop-schema.sql", "/db/schema.sql", "/db/data.sql"})
    void test_getById_givenExistingUserId_thenReturn() {

        // Given
        final var sut = createSUT();
        final var expectedUser = user1();
        final var existingUserId = expectedUser.id();

        // When
        final var result = sut.dao.getById(existingUserId);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    record SUT(UsersReadDao dao) {
    }
}
