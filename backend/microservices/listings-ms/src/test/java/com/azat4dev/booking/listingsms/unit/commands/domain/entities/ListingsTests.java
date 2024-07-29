package com.azat4dev.booking.listingsms.unit.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.entities.ListingFactory;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listings;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsImpl;
import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExtractTraceContext;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReader;
import com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.azat4dev.booking.listingsms.unit.commands.domain.entities.DomainHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class ListingsTests {

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

        final var extractTraceContext = mock(ExtractTraceContext.class);
        final var outboxEventsReader = mock(OutboxEventsReader.class);

        final var factory = mock(ListingFactory.class);

        return new SUT(
            new ListingsImpl(
                unitOfWorkFactory,
                outboxEventsReader::trigger,
                timeProvider,
                extractTraceContext,
                factory
            ),
            factory,
            unitOfWork,
            listingsRepository,
            outboxRepository,
            timeProvider,
            extractTraceContext
        );
    }

    @Test
    void test_addNew_givenValidData_thenPutNewListingInRepositoryAndPublishSuccessEvent()
        throws ListingsRepository.Exception.ListingAlreadyExists {

        // Given
        var sut = createSUT();
        final var listingId = anyListingId();
        final var hostId = anyHostId();
        final var title = anyListingTitle();
        final var tracingInfo = "tracingInfo";

        final var now = LocalDateTime.now();

        given(sut.timeProvider.currentTime())
            .willReturn(now);

        given(sut.extractTraceContext.execute())
            .willReturn(tracingInfo);

        // When
        sut.listings.addNew(
            listingId,
            hostId,
            title
        );

        // Then
        final var expectedListing = sut.listingFactory.makeNewDraft(
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
            .publish(expectedEvent, tracingInfo);

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
            .publish(any(), any());

        // When

        final var exception = assertThrows(RuntimeException.class, () -> {
            sut.listings.addNew(
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
        Listings listings,
        ListingFactory listingFactory,
        UnitOfWork unitOfWork,
        ListingsRepository listingsRepository,
        OutboxEventsRepository outboxRepository,
        TimeProvider timeProvider,
        ExtractTraceContext extractTraceContext
    ) {
    }
}
