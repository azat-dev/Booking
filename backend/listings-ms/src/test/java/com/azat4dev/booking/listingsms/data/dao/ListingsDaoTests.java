package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.application.config.data.ListingsDaoConfig;
import com.azat4dev.booking.listingsms.data.dao.entities.ListingData;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ListingsDaoConfig.class)
public class ListingsDaoTests implements PostgresTests {

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    ListingsDao dao;

    @Test
    void test_addNew() {
        // Given
        final var listingId = UUID.randomUUID();
        final var ownerId = UUID.randomUUID();

        final var newListing = new ListingData(
            listingId,
            LocalDateTime.now(),
            LocalDateTime.now(),
            ownerId,
            "Test Listing",
            "DRAFT",
            Optional.of("Description")
        );

        // When
        dao.addNew(newListing);

        // Then
        final var foundListing = dao.findById(listingId).orElseThrow();

        assertThat(foundListing.id()).isEqualTo(listingId);
        assertThat(foundListing.title()).isEqualTo(newListing.title());
        assertThat(foundListing.status()).isEqualTo(newListing.status());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
