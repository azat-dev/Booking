package com.azat4dev.booking.listingsms.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalogImpl;
import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWork;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ListingCatalogTests {

    private SUT createSUT() {

        final var unitOfWork = mock(UnitOfWork.class);

        final var listingsRepository = mock(ListingsRepository.class);
        final var outboxRepository = mock(OutboxEventsRepository.class);

        return new SUT(
            new ListingsCatalogImpl(
                unitOfWork
            ),
            unitOfWork,
            listingsRepository,
            outboxRepository
        );
    }

    @Test
    void test_addNew_givenValidData_thenPutNewListingInRepositoryAndPublishSuccessEvent() {

        // Given
        var sut = createSUT();
        final var listingId = DomainHelpers.anyListingId();
        final var ownerId = DomainHelpers.anyOwnerId();
        final var title = DomainHelpers.anyListingTitle();

        // When
        sut.listingsCatalog.addNew(
            listingId,
            ownerId,
            title
        );

        // Then
        final var expectedListing = new Listing(
            listingId,
            ownerId,
            title
        );

        then(sut.listingsRepository).should(times(1))
            .addNew(expectedListing);

        final var expectedEvent = new NewListingAdded(
            listingId,
            ownerId,
            title
        );

        then(sut.outboxRepository).should(times(1))
            .publish(expectedEvent);

        then(sut.unitOfWork).should(times(1))
            .save();
    }

    @Test
    void test_addNew_givenErrorDuringSaving_thenRollbackAndThrowException() {

        // Given
        var sut = createSUT();
        final var listingId = DomainHelpers.anyListingId();
        final var ownerId = DomainHelpers.anyOwnerId();
        final var title = DomainHelpers.anyListingTitle();

        willThrow(new RuntimeException())
            .given(sut.outboxRepository)
            .publish(any());

        // When

        final var exception = assertThrows(RuntimeException.class, () -> {
            sut.listingsCatalog.addNew(
                listingId,
                ownerId,
                title
            );
        });

        // Then

        then(sut.unitOfWork).should(times(1))
            .rollback();

        assertThat(exception).isNotNull();
    }

    private record SUT(
        ListingsCatalog listingsCatalog,
        UnitOfWork unitOfWork,
        ListingsRepository listingsRepository,
        OutboxEventsRepository outboxRepository
    ) {
    }
}
