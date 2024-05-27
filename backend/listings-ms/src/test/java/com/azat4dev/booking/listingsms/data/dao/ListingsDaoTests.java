package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.application.config.data.DaoConfig;
import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DaoConfig.class)
@JdbcTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ListingsDaoTests  {

    @Autowired
    ListingsDao dao;

    ListingData anyListingData() {
        return new ListingData(
            UUID.randomUUID(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            UUID.randomUUID(),
            Faker.instance().name().title(),
            "DRAFT",
            Optional.of(Faker.instance().lorem().toString())
        );
    }

    @Test
    @Sql(scripts = {"/db/drop-schema.sql"})
    @Sql(scripts = {"/db/schema.sql"})
    void test_addNew() {
        // Given
        final var listingId = UUID.randomUUID();
        final var ownerId = UUID.randomUUID();

        final var newListing = anyListingData();

        // When
        dao.addNew(newListing);

        // Then
        final var foundListing = dao.findById(listingId).orElseThrow();

        assertThat(foundListing.id()).isEqualTo(listingId);
        assertThat(foundListing.title()).isEqualTo(newListing.title());
        assertThat(foundListing.status()).isEqualTo(newListing.status());
    }

    @Test
    @Sql(scripts = {"/db/drop-schema.sql"})
    @Sql(scripts = {"/db/schema.sql"})
    void test_findById_givenExistingId_thenReturnListing() {
        // Given
        final var listing1 = anyListingData();
        final var listing2 = anyListingData();
        final var listing3 = anyListingData();


        dao.addNew(listing1);
        dao.addNew(listing2);
        dao.addNew(listing3);

        // When
        final var foundListing = dao.findById(listing2.id());

        // Then
        assertThat(foundListing).isPresent();
        assertThat(foundListing.get().id()).isEqualTo(listing2.id());
        assertThat(foundListing.get().title()).isEqualTo(listing2.title());
        assertThat(foundListing.get().status()).isEqualTo(listing2.status());
        assertThat(foundListing.get().description()).isEqualTo(listing2.description());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
