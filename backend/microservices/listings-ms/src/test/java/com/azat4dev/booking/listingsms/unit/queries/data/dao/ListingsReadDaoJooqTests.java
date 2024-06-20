package com.azat4dev.booking.listingsms.unit.queries.data.dao;

import com.azat4dev.booking.listingsms.config.queries.infrastructure.persitence.dao.ListingsReadDaoConfig;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.JSON;
import org.jooq.generated.tables.records.ListingsRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ListingsReadDaoConfig.class)
@Sql("/db/schema.sql")
@JooqTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ListingsReadDaoJooqTests {

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
        final var hostId = UUID.fromString("00000000-0000-0000-0000-000000000010");

        // When
        final var foundListing = dao.findById(listingId).orElseThrow();

        // Then
        final var expectedListing = new ListingsRecord(
            listingId,
            LocalDateTime.of(2024, 5, 8, 12, 0, 0),
            1,
            LocalDateTime.of(2024, 5, 8, 12, 0, 0),
            2,
            hostId,
            "listing1",
            "description1",
            "DRAFT",
            JSON.json("""
                [{"id": "photo1", "bucketName": "bucket1", "objectName": "object1"}]
                """.trim()),
            1,
            2,
            3,
            "APARTMENT",
            "PRIVATE_ROOM",
            "street1",
            "city1",
            "country1"
        );

        assertThat(foundListing).isEqualTo(expectedListing);
    }

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    @Sql("/db/data.sql")
    void test_findAllByHostId_givenExistingHostId_thenReturnAllRecords() {
        // Given
        final var hostId = UUID.fromString("00000000-0000-0000-0000-000000000010");

        // When
        final var foundListings = dao.findAllByHostId(hostId);

        // Then
        assertThat(foundListings.size()).isEqualTo(2);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
