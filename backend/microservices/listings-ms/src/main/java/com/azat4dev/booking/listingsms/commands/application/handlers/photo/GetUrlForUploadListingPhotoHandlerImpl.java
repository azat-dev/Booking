package com.azat4dev.booking.listingsms.commands.application.handlers.photo;

import com.azat4dev.booking.listingsms.commands.application.commands.GetUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Observed
@AllArgsConstructor
public class GetUrlForUploadListingPhotoHandlerImpl implements GetUrlForUploadListingPhotoHandler {

    private final GenerateUrlForUploadListingPhoto generateUrlForUploadUserPhoto;

    @Override
    public GeneratedUrlForUploadListingPhoto handle(GetUrlForUploadListingPhoto command) throws Exception.FailedGenerate {
        try {

            final var result = generateUrlForUploadUserPhoto.execute(
                IdempotentOperationId.checkAndMakeFrom(command.operationId()),
                UserId.checkAndMakeFrom(command.userId()),
                ListingId.checkAndMakeFrom(command.listingId()),
                PhotoFileExtension.checkAndMakeFrom(command.fileExtension()),
                command.fileSize()
            );

            log.atInfo()
                .addKeyValue("operationId", command::operationId)
                .addKeyValue("userId", command::userId)
                .addKeyValue("listingId", command::listingId)
                .addKeyValue("fileExtension", command::fileExtension)
                .addKeyValue("fileSize", command::fileSize)
                .log("Generated URL for upload listing photo");

            return result;

        } catch (PhotoFileExtension.InvalidPhotoFileExtensionException e) {
            log.atWarn()
                .addKeyValue("userId", command::userId)
                .addKeyValue("fileExtension", command::fileExtension)
                .addKeyValue("errorMessage", e::getMessage)
                .addKeyValue("code", e::getCode)
                .log("Invalid file extension");
            throw ValidationException.withPath("fileExtension", e);
        } catch (IdempotentOperationId.Exception e) {
            log.atWarn()
                .addKeyValue("userId", command::userId)
                .addKeyValue("operationId", command::operationId)
                .addKeyValue("errorMessage", e::getMessage)
                .addKeyValue("code", e::getCode)
                .log("Invalid operation ID");
            throw ValidationException.withPath("operationId", e);
        } catch (GenerateUrlForUploadListingPhoto.Exception.WrongFileSize e) {
            log.atWarn()
                .addKeyValue("userId", command::userId)
                .addKeyValue("fileSize", command::fileSize)
                .addKeyValue("errorMessage", e::getMessage)
                .addKeyValue("code", e::getCode)
                .log("Invalid file size");
            throw ValidationException.withPath("fileSize", e);
        } catch (GenerateUrlForUploadListingPhoto.Exception.FailedGenerate e) {
            log.atError()
                .addKeyValue("userId", command::userId)
                .setCause(e)
                .log("Failed to generate URL for upload listing photo");
            throw new Exception.FailedGenerate();
        } catch (UserId.WrongFormatException e) {
            log.atWarn()
                .addKeyValue("userId", command::userId)
                .addKeyValue("errorMessage", e::getMessage)
                .addKeyValue("code", e::getCode)
                .log("Invalid user ID");
            throw ValidationException.withPath("hostId", e);
        } catch (ListingId.Exception.WrongFormat e) {
            log.atWarn()
                .addKeyValue("listingId", command::listingId)
                .addKeyValue("errorMessage", e::getMessage)
                .addKeyValue("code", e::getCode)
                .log("Invalid listing ID");
            throw ValidationException.withPath("listingId", e);
        }
    }
}
