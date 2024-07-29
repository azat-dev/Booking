package com.azat4dev.booking.listingsms.unit.commands.handlers;

import com.azat4dev.booking.listingsms.commands.application.commands.AddNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.application.handlers.photo.AddNewPhotoToListingHandler;
import com.azat4dev.booking.listingsms.commands.application.handlers.photo.AddNewPhotoToListingHandlerImpl;
import com.azat4dev.booking.listingsms.commands.domain.entities.*;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhotoId;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyListingId;
import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyUserId;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class AddNewPhotoToListingHandlerImplTests {

    private SUT createSUT() {

        var host = mock(Host.class);
        var hosts = mock(Hosts.class);
        var hostListings = mock(HostListings.class);
        var listing = mock(ListingImpl.class);

        var listingsCatalog = mock(Listings.class);

        given(hosts.getById(any())).willReturn(host);
        given(host.getListings()).willReturn(hostListings);

        given(hostListings.findById(any()))
            .willReturn(Optional.of(listing));

        return new SUT(
            new AddNewPhotoToListingHandlerImpl(
                hosts,
                listingsCatalog
            ),
            hosts,
            host,
            hostListings,
            listing,
            listingsCatalog
        );
    }

    private AddNewPhotoToListing anyCommand() {
        return new AddNewPhotoToListing(
            anyUserId(),
            "operationId",
            anyListingId().toString(),
            new AddNewPhotoToListing.UploadedFileData(
                "bucket1",
                "photo.jpg"
            )
        );
    }

    @Test
    void test_handle_givenNotExistingListing_thenThrowException() {

        // Given
        var sut = createSUT();
        final var command = anyCommand();
        final var listingId = ListingId.dangerouslyMakeFrom(command.listingId());

        given(sut.hostListings.findById(any()))
            .willReturn(Optional.empty());

        // When

        assertThrows(
            AddNewPhotoToListingHandler.Exception.ListingNotFound.class,
            () -> sut.handler.handle(command)
        );

        // Then
        then(sut.hostListings).should(times(1))
            .findById(listingId);
    }

    @Test
    void test_handle_givenExistingListing_thenAdd() throws ListingImpl.Exception.MaxPhotosReached, AddNewPhotoToListingHandler.Exception.AccessForbidden, AddNewPhotoToListingHandler.Exception.PhotoAlreadyExists, AddNewPhotoToListingHandler.Exception.MaxPhotosReached, AddNewPhotoToListingHandler.Exception.ListingNotFound, AddNewPhotoToListingHandler.Exception.PhotoNotFound, Listings.Exception.ListingNotFound {

        // Given
        var sut = createSUT();
        final var command = anyCommand();

        final var hostId = HostId.fromUserId(command.userId());
        final var listingId = ListingId.dangerouslyMakeFrom(command.listingId());

        final var expectedPhotoId = ListingPhotoId.generateNew();

        final var expectedListingPhoto = new ListingPhoto(
            expectedPhotoId,
            BucketName.makeWithoutChecks(command.uploadedFileData().bucketName()),
            MediaObjectName.dangerouslyMake(command.uploadedFileData().objectName())
        );

        given(sut.listing.addNewPhoto(any(), any()))
            .willReturn(expectedPhotoId);

        // When
        sut.handler.handle(command);

        // Then
        then(sut.hosts).should(times(1))
            .getById(hostId);

        then(sut.hostListings).should(times(1))
            .findById(listingId);

        then(sut.listing).should(times(1))
            .addNewPhoto(expectedListingPhoto.getBucketName(), expectedListingPhoto.getObjectName());

        then(sut.listings).should(times(1))
            .update(sut.listing);
    }

    private record SUT(
        AddNewPhotoToListingHandler handler,
        Hosts hosts,
        Host host,
        HostListings hostListings,
        ListingImpl listing,
        Listings listings
    ) {
    }
}