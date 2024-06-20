package com.azat4dev.booking.users.queries.infrastructure.dao;

import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.UsersReadDao;
import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.UsersReadDaoJdbc;
import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.records.UserRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsersReadDaoJdbcTests {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    SUT createSUT() {
        return new SUT(
            new UsersReadDaoJdbc(
                jdbcTemplate,
                new ObjectMapper()
            )
        );
    }

    UserRecord user1() {
        return new UserRecord(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "john@example.com",
            UserRecord.EmailVerificationStatus.VERIFIED,
            "John",
            "Doe",
            Optional.of(
                new UserRecord.PhotoPath(
                    "bucket1",
                    "myobjectkey1"
                )
            )
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
