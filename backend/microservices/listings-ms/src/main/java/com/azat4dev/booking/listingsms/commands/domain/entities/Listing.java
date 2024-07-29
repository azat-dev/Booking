package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Listing {

    int MINIMUM_NUMBER_OF_PHOTOS = 5;
    int MAX_NUMBER_OF_PHOTOS = 20;

    ListingId getId();

    HostId getHostId();

    List<ListingPhoto> getPhotos();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    void publish() throws Listing.Exception.Publishing;

    ListingStatus getStatus();

    void setTitle(ListingTitle title) throws Listing.Exception.CantModifyPublishedListing;

    ListingTitle getTitle();

    void setDescription(Optional<ListingDescription> description) throws Listing.Exception.CantModifyPublishedListing;

    Optional<ListingDescription> getDescription();

    void setPropertyType(Optional<PropertyType> propertyType) throws Listing.Exception.CantModifyPublishedListing;

    Optional<PropertyType> getPropertyType();

    void setRoomType(Optional<RoomType> roomType) throws Listing.Exception.CantModifyPublishedListing;

    Optional<RoomType> getRoomType();

    void setGuestsCapacity(GuestsCapacity guestsCapacity) throws Listing.Exception.CantModifyPublishedListing;

    GuestsCapacity getGuestsCapacity();

    void setAddress(Optional<ListingAddress> address) throws Listing.Exception.CantModifyPublishedListing;

    Optional<ListingAddress> getAddress();

    boolean isReadyForPublishing();

    void internalSetUpdatedAt(LocalDateTime updatedAt);

    ListingPhotoId addNewPhoto(
        BucketName bucketName,
        MediaObjectName objectName
    ) throws Listing.Exception.MaxPhotosReached;

    // Exceptions

    class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static class CantModifyPublishedListing extends Listing.Exception {
            public CantModifyPublishedListing() {
                super("Can't modify published listing");
            }
        }

        public static class MaxPhotosReached extends Exception {
            public MaxPhotosReached() {
                super("Maximum number of photos reached. Can't add more than " + MAX_NUMBER_OF_PHOTOS + " photos");
            }
        }

        public abstract static class Publishing extends Exception {

            protected Publishing(String message) {
                super(message);
            }

            public static final class DescriptionIsRequired extends Publishing {
                public DescriptionIsRequired() {
                    super("Description is required");
                }
            }

            public static final class AddressIsRequired extends Publishing {
                public AddressIsRequired() {
                    super("Address is required");
                }
            }

            public static final class PropertyTypeIsRequired extends Publishing {
                public PropertyTypeIsRequired() {
                    super("Property type is required");
                }
            }

            public static final class RoomTypeIsRequired extends Publishing {
                public RoomTypeIsRequired() {
                    super("Room type is required");
                }
            }

            public static final class MinimumNumberOfPhotos extends Publishing {
                public MinimumNumberOfPhotos() {
                    super("Minimum " + MINIMUM_NUMBER_OF_PHOTOS + " photo is required");
                }
            }
        }
    }
}
