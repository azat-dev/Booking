package com.azat4dev.booking.listingsms.unit.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalogImpl;
import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.azat4dev.booking.listingsms.unit.commands.domain.entities.DomainHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ListingCatalogTests {

    private SUT createSUT() {

        final var unitOfWork = mock(UnitOfWork.class);

        final var listingsRepository = mock(ListingsRepository.class);
        final var outboxRepository = mock(OutboxEventsRepository.class);

        given(unitOfWork.getListingsRepository())
            .willReturn(listingsRepository);

        given(unitOfWork.getOutboxEventsRepository())
            .willReturn(outboxRepository);

        final var timeProvider = mock(TimeProvider.class);

        final var unitOfWorkFactory = mock(UnitOfWorkFactory.class);
        given(unitOfWorkFactory.make())
            .willReturn(unitOfWork);

        return new SUT(
            new ListingsCatalogImpl(
                unitOfWorkFactory,
                timeProvider
            ),
            unitOfWork,
            listingsRepository,
            outboxRepository,
            timeProvider
        );
    }

    @Test
    void test_addNew_givenValidData_thenPutNewListingInRepositoryAndPublishSuccessEvent() {

        // Given
        var sut = createSUT();
        final var listingId = anyListingId();
        final var hostId = anyHostId();
        final var title = anyListingTitle();

        final var now = LocalDateTime.now();

        given(sut.timeProvider.currentTime())
            .willReturn(now);

        // When
        sut.listingsCatalog.addNew(
            listingId,
            hostId,
            title
        );

        // Then
        final var expectedListing = Listing.makeNewDraft(
            listingId,
            now,
            hostId,
            title
        );

        then(sut.listingsRepository).should(times(1))
            .addNew(expectedListing);

        final var expectedEvent = new NewListingAdded(
            listingId,
            hostId,
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
        final var listingId = anyListingId();
        final var hostId = anyHostId();
        final var title = anyListingTitle();


        willThrow(new RuntimeException())
            .given(sut.outboxRepository)
            .publish(any());

        // When

        final var exception = assertThrows(RuntimeException.class, () -> {
            sut.listingsCatalog.addNew(
                listingId,
                hostId,
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
        OutboxEventsRepository outboxRepository,
        TimeProvider timeProvider
    ) {
    }
}
