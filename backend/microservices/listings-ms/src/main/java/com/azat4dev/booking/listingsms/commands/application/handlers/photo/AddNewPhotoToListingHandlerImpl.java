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
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Observed
@AllArgsConstructor
public class AddNewPhotoToListingHandlerImpl implements AddNewPhotoToListingHandler {

    private final Hosts hosts;
    private final MakeNewListingPhoto makeNewListingPhoto;
    private final ListingsCatalog listingsCatalog;

    @Override
    public void handle(AddNewPhotoToListing command)
        throws Exception.ListingNotFound, Exception.PhotoNotFound,
        Exception.AccessForbidden, Exception.PhotoAlreadyExists, Exception.MaxPhotosReached {

        if (command.userId() == null) {
            log.atWarn()
                .addKeyValue("userId", command::userId)
                .addArgument(command::userId)
                .log("Access forbidden userId: {}");
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

            log.atInfo()
                .addKeyValue("listingId", listingId::getValue)
                .addKeyValue("photoId", listingPhoto::getId)
                .log("Photo added to listing");

        } catch (ListingId.Exception.WrongFormat e) {
            log.atWarn()
                .addKeyValue("listingId", command::listingId)
                .addArgument(command::listingId)
                .log("ListingId wrong format: {}");
            throw ValidationException.withPath("listingId", e);
        } catch (Listing.Exception.MaxPhotosReached e) {
            log.atWarn()
                .addKeyValue("errorMessage", e.getMessage())
                .addKeyValue("listingId", command::listingId)
                .addArgument(command::listingId)
                .log("Max photos reached for listing: {}");
            throw new Exception.MaxPhotosReached();
        } catch (MediaObjectName.InvalidMediaObjectNameException e) {
            log.atWarn()
                .addKeyValue("objectName", command.uploadedFileData()::objectName)
                .addArgument(command.uploadedFileData()::objectName)
                .log("MediaObjectName wrong format: {}");
            throw ValidationException.withPath("uploadedFileData.objectName", e);
        } catch (BucketName.Exception e) {
            log.atWarn()
                .addKeyValue("bucketName", command.uploadedFileData()::bucketName)
                .addArgument(command.uploadedFileData()::bucketName)
                .log("BucketName wrong format: {}");
            throw ValidationException.withPath("uploadedFileData.bucketName", e);
        } catch (ListingsCatalog.Exception.ListingNotFound e) {
            log.atError()
                .addKeyValue("listingId", command::listingId)
                .addKeyValue("userId", command::userId)
                .addArgument(command::listingId)
                .log("Listing not found: {}");
            throw new Exception.ListingNotFound(command.listingId());
        }
    }
}
