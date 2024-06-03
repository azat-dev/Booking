package com.azat4dev.booking.listingsms.unit.commands.handlers;

import com.azat4dev.booking.listingsms.commands.application.commands.AddNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewPhotoToListingHandler;
import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewPhotoToListingHandlerImpl;
import com.azat4dev.booking.listingsms.commands.application.handlers.MakeNewListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.entities.Host;
import com.azat4dev.booking.listingsms.commands.domain.entities.HostListings;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyListingId;
import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyUserId;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class AddNewPhotoToListingHandlerImplTests {

    private SUT createSUT() {

        var host = mock(Host.class);
        var hosts = mock(Hosts.class);
        var hostListings = mock(HostListings.class);
        var listing = mock(Listing.class);
        var makeNewListingPhoto = mock(MakeNewListingPhoto.class);

        given(hosts.getById(any())).willReturn(host);
        given(host.getListings()).willReturn(hostListings);

        return new SUT(
            new AddNewPhotoToListingHandlerImpl(
                hosts,
                makeNewListingPhoto
            ),
            hosts,
            host,
            hostListings,
            listing,
            makeNewListingPhoto
        );
    }

    private AddNewPhotoToListing anyCommand() {
        return new AddNewPhotoToListing(
            "operationid",
            anyUserId().toString(),
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
    void test_handle_givenExistingListing_thenAdd() throws Listing.Exception.MaxPhotosReached, MalformedURLException {

        // Given
        var sut = createSUT();
        final var command = anyCommand();

        final var url = URI.create("http://example.com/photo.jpg").toURL();
        final var hostId = HostId.dangerouslyMakeFrom(command.userId());
        final var listingId = ListingId.dangerouslyMakeFrom(command.listingId());

        final var expectedPhotoId = "photoId";

        final var expectedListingPhoto = new ListingPhoto(
            expectedPhotoId,
            BucketName.makeWithoutChecks(command.uploadedFileData().bucketName()),
            MediaObjectName.dangerouslyMake(command.uploadedFileData().objectName())
        );

        given(sut.hostListings.findById(any()))
            .willReturn(Optional.empty());

        given(sut.makeNewListingPhoto.execute(any()))
            .willReturn(expectedListingPhoto);

        // When
        assertThrows(
            AddNewPhotoToListingHandler.Exception.ListingNotFound.class,
            () -> sut.handler.handle(command)
        );

        // Then
        then(sut.hosts).should(times(1))
            .getById(hostId);

        then(sut.hostListings).should(times(1))
            .findById(listingId);

        then(sut.listing).should(times(1))
            .addPhoto(expectedListingPhoto);
    }

    private record SUT(
        AddNewPhotoToListingHandler handler,
        Hosts hosts,
        Host host,
        HostListings hostListings,
        Listing listing,
        MakeNewListingPhoto makeNewListingPhoto
    ) {
    }
}