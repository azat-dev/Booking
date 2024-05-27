package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.shared.domain.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@EqualsAndHashCode(of = "id")
public class Listing {

    public static final int MINIMUM_NUMBER_OF_PHOTOS = 5;

    private final ListingId id;
    private final OwnerId ownerId;
    private final ListingTitle title;
    private final List<ListingPhoto> photos;
    private ListingStatus status;
    private Optional<ListingDescription> description;

    public Listing(
        ListingId id,
        OwnerId ownerId,
        ListingTitle title
    ) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.status = ListingStatus.DRAFT;
        this.photos = new LinkedList<>();
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
