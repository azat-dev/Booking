package com.azat4dev.booking.listingsms.queries.data.dao;

import com.azat4dev.booking.listingsms.queries.application.config.data.DaoConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DaoConfig.class)
@Sql("/db/schema.sql")
@JdbcTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ListingsReadDaoJdbcTests {

    @Autowired
    ListingsReadDao dao;

    UUID anyRecordId() {
        return UUID.randomUUID();
    }

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    void test_findById_givenEmptyDb_thenReturnEmpty() {
        // Given
        final var listingId = anyRecordId();

        // When
        final var result = dao.findById(listingId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    @Sql("/db/data.sql")
    void test_findById_givenValidId_thenReturnRecord() {
        // Given
        final var listingId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        final var ownerId = UUID.fromString("00000000-0000-0000-0000-000000000010");

        // When
        final var foundListing = dao.findById(listingId).orElseThrow();

        // Then
        final var expectedListing = new ListingRecord(
            listingId,
            LocalDateTime.of(2024, 5, 8, 12, 0, 0)
                .withNano(1),
            LocalDateTime.of(2024, 5, 8, 12, 0, 0)
                .withNano(2),
            ownerId,
            "listing1",
            "DRAFT",
            Optional.of("description1")
        );

        assertThat(foundListing).isEqualTo(expectedListing);
    }
}
