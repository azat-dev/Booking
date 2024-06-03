package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.application.commands.GetUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.application.handlers.GetUrlForUploadListingPhotoHandler;
import com.azat4dev.booking.listingsms.generated.server.api.CommandsListingsPhotoApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.GenerateUploadListingPhotoUrlRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.GenerateUploadListingPhotoUrlResponseBody;
import com.azat4dev.booking.listingsms.generated.server.model.UploadedFileDataDTO;
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
    private final CurrentAuthenticatedUserIdProvider currentUserId;

    @Override
    public ResponseEntity<GenerateUploadListingPhotoUrlResponseBody> generateUploadListingPhotoUrl(
        UUID listingId,
        GenerateUploadListingPhotoUrlRequestBody requestBody
    ) {

        final var userId = currentUserId.get()
            .orElseThrow(() -> new ControllerException(HttpStatus.FORBIDDEN, "Host not authenticated"));

        try {
            final var result = getUrlForUploadListingPhotoHandler.handle(
                new GetUrlForUploadListingPhoto(
                    requestBody.getOperationId().toString(),
                    userId.value().toString(),
                    listingId.toString(),
                    requestBody.getFileExtension(),
                    requestBody.getFileSize()
                )
            );

            return ResponseEntity.ok(new GenerateUploadListingPhotoUrlResponseBody(
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
}
