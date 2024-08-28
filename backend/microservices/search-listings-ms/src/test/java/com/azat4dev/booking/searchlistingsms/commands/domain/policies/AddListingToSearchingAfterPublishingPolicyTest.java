package com.azat4dev.booking.searchlistingsms.commands.domain.policies;

import com.azat4dev.booking.searchlistingsms.commands.domain.events.FailedToAddListingToSearch;
import com.azat4dev.booking.searchlistingsms.commands.domain.events.ListingAddedToSearch;
import com.azat4dev.booking.searchlistingsms.commands.domain.events.incoming.ListingPublished;
import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingDetails;
import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingsDetailsService;
import com.azat4dev.booking.searchlistingsms.commands.domain.interfaces.ListingsSearchRepository;
import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.searchlistingsms.common.acl.domain.values.ListingInfo;
import com.azat4dev.booking.searchlistingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.searchlistingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;

class AddListingToSearchingAfterPublishingPolicyTest {

    private SUT createSUT() {
        final var detailsService = mock(ListingsDetailsService.class);
        final var repository = mock(ListingsSearchRepository.class);
        final var bus = mock(DomainEventsBus.class);

        return new SUT(
            new AddListingToSearchingAfterPublishingPolicy(
                detailsService,
                repository,
                bus
            ),
            detailsService,
            repository,
            bus
        );
    }

    private DomainEvent<ListingPublished> anyEvent() throws GuestsCapacity.Exception.CapacityMustBePositive {
        final var g = new RandomEventIdGenerator();
        return new DomainEvent<>(
            g.generate(),
            LocalDateTime.now(),
            new ListingPublished(
                ListingId.dangerouslyMakeFrom(UUID.randomUUID().toString()),
                LocalDateTime.now(),
                new ListingInfo(
                    "Title",
                    "Description",
                    GuestsCapacity.checkAndMake(1, 2, 3),
                    PropertyType.APARTMENT
                )
            )
        );
    }


    @Test
    void test_execute_givenExistingDetails_thenAddListingToSearch() throws GuestsCapacity.Exception.CapacityMustBePositive {
        // Given
        final var sut = createSUT();
        final var event = anyEvent();
        final var listingId = event.payload().listingId();

        final var existingDetails = new ListingDetails(
            "Title",
            "Description",
            List.of(
                new ListingPhoto(
                    "photoId",
                    BucketName.makeWithoutChecks("bucketName"),
                    MediaObjectName.dangerouslyMake("objectId")
                )
            )
        );

        given(sut.listingsDetailsService.getDetailsFor(listingId))
            .willReturn(existingDetails);

        // When
        sut.policy.execute(event.payload(), event.id(), event.issuedAt());

        // Then
        then(sut.listingsSearchRepository).should(times(1))
            .addNew(listingId, existingDetails.title(), existingDetails.description(), existingDetails.photos());

        then(sut.bus).should(times(1))
            .publish(new ListingAddedToSearch(listingId));
    }

    @Test
    void test_execute_givenExceptionDuringGettingDetails_thenPublishFailEvent() {
        // Given
        final var sut = createSUT();
        final var event = anyEvent();
        final var listingId = event.payload().listingId();

        willThrow(new RuntimeException())
            .given(sut.listingsSearchRepository)
            .addNew(any(), any(), any(), any());

        // When
        sut.policy.execute(event.payload(), event.id(), event.issuedAt());

        // Then
        then(sut.bus).should(times(1))
            .publish(new FailedToAddListingToSearch(listingId));
    }

    private record SUT(
        AddListingToSearchingAfterPublishingPolicy policy,
        ListingsDetailsService listingsDetailsService,
        ListingsSearchRepository listingsSearchRepository,
        DomainEventsBus bus
    ) {
    }
}