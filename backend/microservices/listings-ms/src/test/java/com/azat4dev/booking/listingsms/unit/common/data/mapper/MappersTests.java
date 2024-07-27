package com.azat4dev.booking.listingsms.unit.common.data.mapper;

import com.azat4dev.booking.listingsms.commands.domain.events.*;
import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers.MapGuestsCapacity;
import com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers.MapListingAddress;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.infrastructure.serializers.MapLocalDateTime;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.azat4dev.booking.listingsms.unit.commands.domain.entities.DomainHelpers.anyListingId;
import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyHostId;
import static com.azat4dev.booking.listingsms.unit.helpers.ListingHelpers.anyUserId;
import static org.assertj.core.api.Assertions.assertThat;


class MappersTests {

    IdempotentOperationId anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return IdempotentOperationId.checkAndMakeFrom(UUID.randomUUID().toString());
    }

    @Test
    void test_mappers() throws Exception {

        // Given

        final var mapDateTime = new MapLocalDateTime();
        final var mapGuestCapacity = new MapGuestsCapacity();
        final var mapAddress = new MapListingAddress();

        final var mapListingChange = new MapListingDetailsUpdatedChange(
            mapGuestCapacity,
            mapAddress
        );

        final var mappers = new MapDomainEvent[]{
            new MapNewListingAdded(),
            new MapFailedToAddNewListing(),
            new MapListingDetailsUpdated(mapListingChange, mapDateTime),
            new MapListingPublished(mapDateTime),
            new MapGeneratedUrlForUploadListingPhoto(),
            new MapFailedGenerateUrlForUploadListingPhoto(),
            new MapAddedNewPhotoToListing(),
        };

        final var now = LocalDateTime.now();

        final var events = new DomainEventPayload[]{
            new NewListingAdded(
                anyListingId(),
                anyHostId(),
                ListingTitle.dangerouslyMakeFrom("title")
            ),
            new FailedToAddNewListing(
                anyListingId(),
                anyHostId(),
                ListingTitle.dangerouslyMakeFrom("title")
            ),

            new GeneratedUrlForUploadListingPhoto(
                anyUserId(),
                anyListingId(),
                new UploadFileFormData(
                    new java.net.URL("https://example.com"),
                    BucketName.checkAndMake("bucket"),
                    MediaObjectName.checkAndMakeFrom("object"),
                    Map.of("key1", "value1")
                )
            ),

            new FailedGenerateUrlForUploadListingPhoto(
                anyIdempotentOperationId(),
                anyUserId(),
                anyListingId(),
                PhotoFileExtension.checkAndMakeFrom("jpg"),
                100

            ),

            new AddedNewPhotoToListing(
                anyListingId(),
                new ListingPhoto(
                    "photoId",
                    BucketName.checkAndMake("bucket"),
                    MediaObjectName.checkAndMakeFrom("object")
                )
            ),

            new ListingPublished(
                anyListingId(),
                now
            ),
            new ListingDetailsUpdated(
                anyListingId(),
                now,
                new ListingDetailsUpdated.Change(
                    OptionalField.present(ListingStatus.DRAFT),
                    OptionalField.present(ListingTitle.dangerouslyMakeFrom("title")),
                    OptionalField.present(Optional.of(ListingDescription.dangerouslyMakeFrom("description"))),
                    OptionalField.present(Optional.of(PropertyType.APARTMENT)),
                    OptionalField.present(Optional.of(RoomType.ENTIRE_PLACE)),
                    OptionalField.present(GuestsCapacity.DEFAULT),
                    OptionalField.missed()
                ),
                new ListingDetailsUpdated.Change(
                    OptionalField.present(ListingStatus.DRAFT),
                    OptionalField.present(ListingTitle.dangerouslyMakeFrom("title")),
                    OptionalField.present(Optional.of(ListingDescription.dangerouslyMakeFrom("description"))),
                    OptionalField.present(Optional.of(PropertyType.APARTMENT)),
                    OptionalField.present(Optional.of(RoomType.ENTIRE_PLACE)),
                    OptionalField.present(GuestsCapacity.DEFAULT),
                    OptionalField.present(
                        Optional.of(
                            ListingAddress.dangerouslyMakeFrom(
                                Country.dangerouslyMakeFrom("country"),
                                City.dangerouslyMakeFrom("city"),
                                Street.dangerouslyMakeFrom("street")
                            )
                        )
                    )
                )
            )
        };

        for (final var mapper : mappers) {
            final var event = Arrays.stream(events).filter(e -> e.getClass()
                    .equals(mapper.getOriginalClass()))
                .findFirst().get();

            // When
            test_mapping(mapper, event);
        }
    }

    <E extends DomainEventPayload, D> void test_mapping(Serializer<E, D> serializer, E event) {

        // Given

        // When
        final var dto = serializer.serialize(event);
        final var deserializedEvent = serializer.deserialize(dto);

        // Then
        assertThat(deserializedEvent).isEqualTo(event);
    }
}