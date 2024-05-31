package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.commands.application.config.data.ListingsDaoConfig;
import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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

@Import({ListingsDaoConfig.class})
@JooqTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ListingsDaoTests {

    @Autowired
    ListingsDao dao;

    ListingsRecord anyListingData() {

        final var o = new ListingsRecord();

        o.setId(UUID.randomUUID());
        o.setCreatedAt(LocalDateTime.now());
        o.setCreatedAtNano(1);

        o.setUpdatedAt(LocalDateTime.now());
        o.setUpdatedAtNano(2);

        o.setOwnerId(UUID.randomUUID());
        o.setTitle(Faker.instance().name().title());
        o.setStatus("DRAFT");
        o.setDescription(Faker.instance().lorem().toString());
        o.setPropertyType("APARTMENT");
        o.setRoomType("PRIVATE");
        o.setGuestsCapacityAdults(1);
        o.setGuestsCapacityChildren(2);
        o.setGuestsCapacityInfants(3);
        o.setAddressCountry("KZ");
        o.setAddressCity("Almaty");
        o.setAddressStreet("Kabanbay Batyr");
        o.setPhotos(JSON.json(
            """
                [
                    {"id": "1", "bucket": "bucket", "object": "object"},
                    {"id": "2", "bucket": "bucket", "object": "object"}
                ]
                """
        ));

        return o;
    }

    @Test
    @Sql(scripts = {"/db/drop-schema.sql"})
    @Sql(scripts = {"/db/schema.sql"})
    void test_addNew() throws ListingsDao.Exception.ListingAlreadyExists {
        // Given
        final var newListing = anyListingData();
        final var listingId = newListing.getId();

        // When
        dao.addNew(newListing);

        // Then
        final var foundListing = dao.findById(listingId).orElseThrow();

        assertThat(foundListing.getId()).isEqualTo(listingId);
        assertThat(foundListing.getTitle()).isEqualTo(newListing.getTitle());
        assertThat(foundListing.getStatus()).isEqualTo(newListing.getStatus());
    }

    @Test
    @Sql(scripts = {"/db/drop-schema.sql"})
    @Sql(scripts = {"/db/schema.sql"})
    void test_findById_givenExistingId_thenReturnListing() throws ListingsDao.Exception.ListingAlreadyExists {
        // Given
        final var listing1 = anyListingData();
        final var listing2 = anyListingData();
        final var listing3 = anyListingData();


        dao.addNew(listing1);
        dao.addNew(listing2);
        dao.addNew(listing3);

        // When
        final var foundListing = dao.findById(listing2.getId());

        // Then
        assertThat(foundListing).isPresent();
        assertThat(foundListing.get().getId()).isEqualTo(listing2.getId());
        assertThat(foundListing.get().getTitle()).isEqualTo(listing2.getTitle());
        assertThat(foundListing.get().getStatus()).isEqualTo(listing2.getStatus());
        assertThat(foundListing.get().getDescription()).isEqualTo(listing2.getDescription());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
