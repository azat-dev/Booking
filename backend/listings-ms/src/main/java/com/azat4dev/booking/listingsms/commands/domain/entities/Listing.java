package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.shared.domain.DomainException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Listing {

    public static final int MINIMUM_NUMBER_OF_PHOTOS = 5;

    private final ListingId id;
    private ListingStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final OwnerId ownerId;
    private final ListingTitle title;
    private Optional<List<ListingPhoto>> photos;
    private Optional<ListingDescription> description;

    public static Listing makeNewDraft(
        ListingId id,
        LocalDateTime createdAt,
        OwnerId ownerId,
        ListingTitle title
    ) {

        return new Listing(
            id,
            ListingStatus.DRAFT,
            createdAt,
            createdAt,
            ownerId,
            title,
            Optional.empty(),
            Optional.empty()
        );
    }

    public static Listing internalMake(
        ListingId id,
        ListingStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        OwnerId ownerId,
        ListingTitle title,
        Optional<List<ListingPhoto>> photos,
        Optional<ListingDescription> description
    ) {

        return new Listing(
            id,
            status,
            createdAt,
            updatedAt,
            ownerId,
            title,
            photos,
            description
        );
    }

    private void validateFieldsBeforePublishing() throws Exception.DescriptionIsRequired, Exception.MinimumNumberOfPhotos {

        if (title == null) {
            throw new IllegalStateException("Title is required");
        }

        this.description.orElseThrow(Exception.DescriptionIsRequired::new);

        if (this.photos.isPresent() && this.photos.get().size() < MINIMUM_NUMBER_OF_PHOTOS) {
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
