package com.azat4dev.booking.listingsms.unit.common.data;

import com.azat4dev.booking.listingsms.commands.data.serializer.DomainEventsSerializerImpl;
import com.azat4dev.booking.listingsms.commands.domain.events.*;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.azat4dev.booking.listingsms.unit.commands.domain.entities.DomainHelpers.anyListingId;
import static com.azat4dev.booking.listingsms.unit.helpers.EventHelpers.eventsFactory;
import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyHostId;
import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyUserId;
import static org.assertj.core.api.Assertions.assertThat;


public class DomainEventSerializerImplTests {

    DomainEventSerializer createSUT() {
        final var objectMapper = new ObjectMapper();
        return new DomainEventsSerializerImpl(objectMapper);
    }

    @Test
    void test_serialize() throws MalformedURLException, PhotoFileExtension.InvalidPhotoFileExtensionException, IdempotentOperationId.Exception, BucketName.Exception, MediaObjectName.InvalidMediaObjectNameException {

        final var now = LocalDateTime.now().withNano(0);

        final var events = List.of(
            eventsFactory.issue(
                new NewListingAdded(
                    anyListingId(),
                    anyHostId(),
                    ListingTitle.dangerouslyMakeFrom("title")
                )
            ),
            eventsFactory.issue(
                new FailedToAddNewListing(
                    anyListingId(),
                    anyHostId(),
                    ListingTitle.dangerouslyMakeFrom("title")
                )
            ),

            eventsFactory.issue(
                new GeneratedUrlForUploadListingPhoto(
                    anyUserId(),
                    anyListingId(),
                    new UploadFileFormData(
                        new java.net.URL("https://example.com"),
                        BucketName.checkAndMake("bucket"),
                        MediaObjectName.checkAndMakeFrom("object"),
                        Map.of("key1", "value1")
                    )
                )
            ),

            eventsFactory.issue(
                new FailedGenerateUrlForUploadListingPhoto(
                    IdempotentOperationId.checkAndMakeFrom("operationId"),
                    anyUserId(),
                    anyListingId(),
                    PhotoFileExtension.checkAndMakeFrom("jpg"),
                    100
                )
            ),

            eventsFactory.issue(
                new AddedNewPhotoToListing(
                    anyListingId(),
                    new ListingPhoto(
                        "photoId",
                        BucketName.checkAndMake("bucket"),
                        MediaObjectName.checkAndMakeFrom("object")
                    )
                )
            ),

            eventsFactory.issue(
                new ListingDetailsUpdated(
                    anyListingId(),
                    now,
                    new ListingDetailsUpdated.Change(
                        Optional.of(ListingStatus.DRAFT),
                        Optional.of(ListingTitle.dangerouslyMakeFrom("title")),
                        Optional.of(Optional.of(ListingDescription.dangerouslyMakeFrom("description"))),
                        Optional.of(Optional.of(PropertyType.APARTMENT)),
                        Optional.of(Optional.of(RoomType.ENTIRE_PLACE)),
                        Optional.of(GuestsCapacity.DEFAULT),
                        Optional.empty()
                    ),
                    new ListingDetailsUpdated.Change(
                        Optional.of(ListingStatus.DRAFT),
                        Optional.of(ListingTitle.dangerouslyMakeFrom("title")),
                        Optional.of(Optional.of(ListingDescription.dangerouslyMakeFrom("description"))),
                        Optional.of(Optional.of(PropertyType.APARTMENT)),
                        Optional.of(Optional.of(RoomType.ENTIRE_PLACE)),
                        Optional.of(GuestsCapacity.DEFAULT),
                        Optional.empty()
                    )
                )
            )
        );

        for (final var event : events) {
            // Given
            final var sut = createSUT();

            // When
            final var serialized = sut.serialize(event);
            final var deserializedValue = sut.deserialize(serialized);

            // Then

            assertThat(serialized).isNotNull();
            assertThat(deserializedValue).isEqualTo(event);
        }
    }
}
