package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.shared.domain.DomainException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Listing {

    public static final int MINIMUM_NUMBER_OF_PHOTOS = 5;

    private final ListingId id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final OwnerId ownerId;
    private final ListingTitle title;
    private ListingStatus status;
    private Optional<ListingDescription> description;
    private Optional<PropertyType> propertyType;
    private Optional<RoomType> roomType;
    private GuestsCapacity guestsCapacity;
    private Optional<ListingAddress> address;
    private List<ListingPhoto> photos;

    public static Listing makeNewDraft(
        ListingId id,
        LocalDateTime createdAt,
        OwnerId ownerId,
        ListingTitle title
    ) {

        return new Listing(
            id,
            createdAt,
            createdAt,
            ownerId,
            title,
            ListingStatus.DRAFT,
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            GuestsCapacity.DEFAULT,
            Optional.empty(),
            List.of()
        );
    }

    public static Listing internalMake(
        ListingId id,
        ListingStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        OwnerId ownerId,
        ListingTitle title,
        Optional<ListingDescription> description,
        Optional<PropertyType> propertyType,
        Optional<RoomType> roomType,
        Optional<ListingAddress> address,
        GuestsCapacity guestsCapacity,
        List<ListingPhoto> photos
    ) {

        return new Listing(
            id,
            createdAt,
            updatedAt,
            ownerId,
            title,
            status,
            description,
            propertyType,
            roomType,
            guestsCapacity,
            address,
            photos
        );
    }

    private void validateFieldsBeforePublishing() throws Exception.DescriptionIsRequired, Exception.MinimumNumberOfPhotos {

        if (title == null) {
            throw new IllegalStateException("Title is required");
        }

        this.description.orElseThrow(Exception.DescriptionIsRequired::new);

        if (this.photos.size() < MINIMUM_NUMBER_OF_PHOTOS) {
            throw new Exception.MinimumNumberOfPhotos();
        }
    }

    public void setStatus(ListingStatus status) throws Exception.MinimumNumberOfPhotos, Exception.DescriptionIsRequired {

        if (status == ListingStatus.PUBLISHED) {
            validateFieldsBeforePublishing();
        }

        this.status = status;
    }

    // Exceptions

    public static class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static class DescriptionIsRequired extends Exception {
            public DescriptionIsRequired() {
                super("Description is required");
            }
        }

        public static class MinimumNumberOfPhotos extends Exception {
            public MinimumNumberOfPhotos() {
                super("Minimum " + MINIMUM_NUMBER_OF_PHOTOS + " photo is required");
            }
        }
    }
}
