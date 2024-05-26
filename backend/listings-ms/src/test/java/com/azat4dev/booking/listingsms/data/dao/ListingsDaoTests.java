package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.commands.core.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.core.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.core.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.core.domain.values.MakeNewListingIdImpl;
import com.azat4dev.booking.listingsms.commands.core.domain.values.OwnerId;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.azat4dev.booking.shared.domain.core.UserIdFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListingsDaoTests implements PostgresTests {

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    OwnerId anyOwnerId() {
        final var userId = new UserIdFactoryImpl().generateNewUserId();
        return OwnerId.dangerouslyMakeFrom(userId.value().toString());
    }

    @Test
    void test_addNew() throws ListingTitle.Exception.WrongFormat, ListingTitle.Exception.MaxLength {
        // Given
        final var listingId = new MakeNewListingIdImpl().make();
        final var ownerId = anyOwnerId();

        final var newListing = new Listing(
            listingId,
            ownerId,
            ListingTitle.checkAndMakeFrom("Test Listing")
        );

        final var dao = new ListingsDaoImpl();

        // When
        dao.addNew(newListing);

        // Then
        final var foundListing = dao.findById(listingId).orElseThrow();

        assertThat(foundListing.getId()).isEqualTo(listingId);
        assertThat(foundListing.getTitle()).isEqualTo(newListing.getTitle());
        assertThat(foundListing.getStatus()).isEqualTo(ListingStatus.DRAFT);
    }
}
