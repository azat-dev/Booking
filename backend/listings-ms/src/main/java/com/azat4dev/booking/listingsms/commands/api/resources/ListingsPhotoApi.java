package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.application.commands.AddNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.application.commands.GetUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.application.handlers.photo.AddNewPhotoToListingHandler;
import com.azat4dev.booking.listingsms.commands.application.handlers.photo.GetUrlForUploadListingPhotoHandler;
import com.azat4dev.booking.listingsms.generated.server.api.CommandsListingsPhotoApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.*;
import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public final class ListingsPhotoApi implements CommandsListingsPhotoApiDelegate {

    private final GetUrlForUploadListingPhotoHandler getUrlForUploadListingPhotoHandler;
    private final AddNewPhotoToListingHandler addNewPhotoToListingHandler;
    private final CurrentAuthenticatedUserIdProvider currentUserId;

    @Override
    public ResponseEntity<GenerateUploadListingPhotoUrlResponseBodyDTO> generateUploadListingPhotoUrl(
        UUID listingId,
        GenerateUploadListingPhotoUrlRequestBodyDTO requestBody
    ) {

        final var userId = currentUserId.get()
            .orElseThrow(() -> new ControllerException(HttpStatus.FORBIDDEN, "Host not authenticated"));

        try {
            final var result = getUrlForUploadListingPhotoHandler.handle(
                new GetUrlForUploadListingPhoto(
                    requestBody.getOperationId().toString(),
                    userId.value().toString(),
                    listingId.toString(),
                    requestBody.getFileExtension().orElse(null),
                    requestBody.getFileSize().orElse(null)
                )
            );

            return ResponseEntity.ok(new GenerateUploadListingPhotoUrlResponseBodyDTO(
                UploadedFileDataDTO.builder()
                    .url(result.formData().url().toString())
                    .bucketName(result.formData().bucketName().getValue())
                    .objectName(result.formData().objectName().getValue())
                    .build(),
                result.formData().formData()
            ));

        } catch (GetUrlForUploadListingPhotoHandler.Exception.FailedGenerate e) {
            throw ControllerException.createError(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public ResponseEntity<AddListingPhotoResponseBodyDTO> addPhotoToListing(UUID listingId, AddListingPhotoRequestBodyDTO requestBody) {


        try {
            final var command = new AddNewPhotoToListing(
                currentUserId.get()
                    .orElseThrow(() -> new ControllerException(HttpStatus.FORBIDDEN, "Host not authenticated")),
                requestBody.getOperationId().toString(),
                listingId.toString(),
                new AddNewPhotoToListing.UploadedFileData(
                    requestBody.getUploadedFile().getBucketName(),
                    requestBody.getUploadedFile().getObjectName()
                )
            );

            addNewPhotoToListingHandler.handle(command);

            return ResponseEntity.ok(new AddListingPhotoResponseBodyDTO());

        } catch (AddNewPhotoToListingHandler.Exception.ListingNotFound |
                 AddNewPhotoToListingHandler.Exception.PhotoNotFound e) {

            throw ControllerException.createError(HttpStatus.NOT_FOUND, e);

        } catch (AddNewPhotoToListingHandler.Exception.AccessForbidden e) {

            throw ControllerException.createError(HttpStatus.FORBIDDEN, e);

        } catch (AddNewPhotoToListingHandler.Exception.PhotoAlreadyExists |
                 AddNewPhotoToListingHandler.Exception.MaxPhotosReached e) {

            throw ControllerException.createError(HttpStatus.CONFLICT, e);
        }
    }
}
