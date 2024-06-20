package com.azat4dev.booking.listingsms.commands.application.handlers.photo;

import com.azat4dev.booking.listingsms.commands.application.commands.AddNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class AddNewPhotoToListingHandlerImpl implements AddNewPhotoToListingHandler {

    private final Hosts hosts;
    private final MakeNewListingPhoto makeNewListingPhoto;
    private final ListingsCatalog listingsCatalog;

    @Override
    public void handle(AddNewPhotoToListing command)
        throws Exception.ListingNotFound, Exception.PhotoNotFound,
        Exception.AccessForbidden, Exception.PhotoAlreadyExists, Exception.MaxPhotosReached {

        if (command.userId() == null) {
            throw new Exception.AccessForbidden();
        }

        final var hostId = HostId.fromUserId(command.userId());
        final var currentHost = hosts.getById(hostId);

        try {
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());

            final var uploadedFileData = command.uploadedFileData();

            final var listingPhoto = makeNewListingPhoto.execute(
                new UploadedFileData(
                    BucketName.checkAndMake(uploadedFileData.bucketName()),
                    MediaObjectName.checkAndMakeFrom(uploadedFileData.objectName())
                )
            );

            final var listing = currentHost.getListings()
                .findById(listingId)
                .orElseThrow(() -> new Exception.ListingNotFound(listingId.getValue().toString()));

            listing.addPhoto(listingPhoto);
            listingsCatalog.update(listing);

        } catch (ListingId.Exception.WrongFormat e) {
            throw ValidationException.withPath("listingId", e);
        } catch (Listing.Exception.MaxPhotosReached e) {
            throw new Exception.MaxPhotosReached();
        } catch (MediaObjectName.InvalidMediaObjectNameException e) {
            throw ValidationException.withPath("uploadedFileData.objectName", e);
        } catch (BucketName.Exception e) {
            throw ValidationException.withPath("uploadedFileData.bucketName", e);
        } catch (ListingsCatalog.Exception.ListingNotFound e) {
            throw new Exception.ListingNotFound(command.listingId());
        }
    }
}
