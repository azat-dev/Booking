package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
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
    private LocalDateTime updatedAt;
    private final OwnerId ownerId;
    private ListingTitle title;
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

    private void validateFieldsBeforePublishing() throws Exception.Publishing {

        if (title == null) {
            throw new Exception.Publishing.DescriptionIsRequired();
        }

        final var description = this.description.orElseThrow(Exception.Publishing.DescriptionIsRequired::new);

        Assert.string(description.getValue(), Exception.Publishing.DescriptionIsRequired::new).notBlank();

        this.address.orElseThrow(Exception.Publishing.AddressIsRequired::new);

        this.propertyType.orElseThrow(Exception.Publishing.PropertyTypeIsRequired::new);

        this.roomType.orElseThrow(Exception.Publishing.RooomTypeIsRequired::new);

        if (this.photos.size() < MINIMUM_NUMBER_OF_PHOTOS) {
            throw new Exception.Publishing.MinimumNumberOfPhotos();
        }
    }

    public void publish() throws Exception.Publishing, Exception.CantModifyPublishedListing {

        validateFieldsBeforePublishing();
        this.status = ListingStatus.PUBLISHED;
    }

    public void setTitle(ListingTitle title) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.title = title);
    }

    public void setDescription(Optional<ListingDescription> description) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.description = description);
    }

    public void setPropertyType(Optional<PropertyType> propertyType) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.propertyType = propertyType);
    }

    public void setRoomType(Optional<RoomType> roomType) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.roomType = roomType);
    }

    public void setGuestsCapacity(GuestsCapacity guestsCapacity) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.guestsCapacity = guestsCapacity);
    }

    public void setAddress(Optional<ListingAddress> address) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.address = address);
    }

    private void updateField(Runnable action) throws Exception.CantModifyPublishedListing {
        cantModifyPublishedListing();
        action.run();
        updateStatusIfNeed();
    }

    private void cantModifyPublishedListing() throws Exception.CantModifyPublishedListing {
        if (this.status == ListingStatus.PUBLISHED) {
            throw new Exception.CantModifyPublishedListing();
        }
    }

    private void updateStatusIfNeed() {
        if (this.status == ListingStatus.PUBLISHED) {
            return;
        }

        if (!isReadyForPublishing()) {
            this.status = ListingStatus.DRAFT;
            return;
        }

        this.status = ListingStatus.READY_FOR_PUBLISHING;
    }

    private boolean isReadyForPublishing() {

        try {
            validateFieldsBeforePublishing();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    // Exceptions

    public static class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static class CantModifyPublishedListing extends Exception {
            public CantModifyPublishedListing() {
                super("Can't modify published listing");
            }
        }

        public static sealed abstract class Publishing extends Exception
            permits Publishing.DescriptionIsRequired, Publishing.AddressIsRequired,
            Publishing.MinimumNumberOfPhotos, Publishing.PropertyTypeIsRequired, Publishing.RooomTypeIsRequired {

            public Publishing(String message) {
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

            public static final class RooomTypeIsRequired extends Publishing {
                public RooomTypeIsRequired() {
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
