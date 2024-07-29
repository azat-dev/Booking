package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.application.handlers.photo.MakeNewListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ListingImpl implements Listing {

    private final ListingId id;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final HostId hostId;
    private ListingTitle title;
    private ListingStatus status;
    private Optional<ListingDescription> description;
    private Optional<PropertyType> propertyType;
    private Optional<RoomType> roomType;
    private GuestsCapacity guestsCapacity;
    private Optional<ListingAddress> address;
    private List<ListingPhoto> photos;
    private MakeNewListingPhoto makeNewListingPhoto;

    public static Listing internalMake(
        ListingId id,
        ListingStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        HostId hostId,
        ListingTitle title,
        Optional<ListingDescription> description,
        Optional<PropertyType> propertyType,
        Optional<RoomType> roomType,
        Optional<ListingAddress> address,
        GuestsCapacity guestsCapacity,
        List<ListingPhoto> photos,
        MakeNewListingPhoto makeNewListingPhoto
    ) {

        return new ListingImpl(
            id,
            createdAt,
            updatedAt,
            hostId,
            title,
            status,
            description,
            propertyType,
            roomType,
            guestsCapacity,
            address,
            photos,
            makeNewListingPhoto
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

        this.roomType.orElseThrow(Exception.Publishing.RoomTypeIsRequired::new);

        if (this.photos.size() < MINIMUM_NUMBER_OF_PHOTOS) {
            throw new Exception.Publishing.MinimumNumberOfPhotos();
        }
    }

    @Override
    public void publish() throws Exception.Publishing {

        validateFieldsBeforePublishing();
        this.status = ListingStatus.PUBLISHED;
    }

    @Override
    public void setTitle(ListingTitle title) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.title = title);
    }

    @Override
    public void setDescription(Optional<ListingDescription> description) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.description = description);
    }

    @Override
    public void setPropertyType(Optional<PropertyType> propertyType) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.propertyType = propertyType);
    }

    @Override
    public void setRoomType(Optional<RoomType> roomType) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.roomType = roomType);
    }

    @Override
    public void setGuestsCapacity(GuestsCapacity guestsCapacity) throws Exception.CantModifyPublishedListing {

        updateField(() -> this.guestsCapacity = guestsCapacity);
    }

    @Override
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

    @Override
    public boolean isReadyForPublishing() {

        try {
            validateFieldsBeforePublishing();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public void internalSetUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public ListingPhotoId addNewPhoto(BucketName bucketName, MediaObjectName objectName) throws Exception.MaxPhotosReached {
        if (this.photos.size() >= MAX_NUMBER_OF_PHOTOS) {
            throw new Exception.MaxPhotosReached();
        }

        final var newPhoto = makeNewListingPhoto.execute(bucketName, objectName);

        final var newPhotos = new LinkedList<>(this.photos);
        newPhotos.add(newPhoto);

        this.photos = Collections.unmodifiableList(newPhotos);
        updateStatusIfNeed();
        return newPhoto.getId();
    }
}
