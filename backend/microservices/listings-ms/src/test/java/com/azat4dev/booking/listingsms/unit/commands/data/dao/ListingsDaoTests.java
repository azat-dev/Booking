package com.azat4dev.booking.listingsms.unit.commands.data.dao;

import com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.dao.ListingsDaoConfig;
import com.azat4dev.booking.listingsms.commands.infrastructure.dao.listings.ListingsDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({ListingsDaoConfig.class})
@JooqTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/db/drop-schema.sql"})
@Sql(scripts = {"/db/schema.sql"})
public class ListingsDaoTests {

    @Autowired
    ListingsDao dao;

    ListingsRecord anyListingData() {

        final var faker = new Faker();
        final var o = new ListingsRecord();

        o.setId(UUID.randomUUID());
        o.setCreatedAt(LocalDateTime.now());
        o.setCreatedAtNano(1);

        o.setUpdatedAt(LocalDateTime.now());
        o.setUpdatedAtNano(2);

        o.setHostId(UUID.randomUUID());
        o.setTitle(faker.name().title());
        o.setStatus("DRAFT");
        o.setDescription(faker.lorem().toString());
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

    @Test
    void test_update_givenListingExists_thanUpdate() throws ListingsDao.Exception.ListingAlreadyExists, ListingsDao.Exception.ListingNotFound {
        // Given
        final var listing1 = anyListingData();
        final var listing2 = anyListingData();
        final var listing3 = anyListingData();

        dao.addNew(listing1);
        dao.addNew(listing2);
        dao.addNew(listing3);

        final var updatedListing1 = anyListingData();
        updatedListing1.setId(listing1.getId());

        // When
        dao.update(updatedListing1);

        // Then
        final var foundListing = dao.findById(listing1.getId()).orElseThrow();
        assertThat(foundListing).isEqualTo(updatedListing1);
    }

    @Test
    void test_update_givenListingDoesntExist_thanThrowException() throws ListingsDao.Exception.ListingAlreadyExists {
        // Given
        final var listing1 = anyListingData();
        final var updatedListing1 = anyListingData();
        updatedListing1.setId(listing1.getId());

        // When
        assertThrows(ListingsDao.Exception.ListingNotFound.class, () -> {
            dao.update(updatedListing1);
        });

        // Then
        // Throws exception
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
