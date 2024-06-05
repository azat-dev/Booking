package com.azat4dev.booking.listingsms.commands.application.handlers.photo;

import com.azat4dev.booking.listingsms.commands.application.commands.GetUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class GetUrlForUploadListingPhotoHandlerImpl implements GetUrlForUploadListingPhotoHandler {

    private final GenerateUrlForUploadListingPhoto generateUrlForUploadUserPhoto;

    @Override
    public GeneratedUrlForUploadListingPhoto handle(GetUrlForUploadListingPhoto command) throws Exception.FailedGenerate {
        try {

            return generateUrlForUploadUserPhoto.execute(
                IdempotentOperationId.checkAndMakeFrom(command.operationId()),
                UserId.checkAndMakeFrom(command.userId()),
                ListingId.checkAndMakeFrom(command.listingId()),
                PhotoFileExtension.checkAndMakeFrom(command.fileExtension()),
                command.fileSize()
            );

        } catch (PhotoFileExtension.InvalidPhotoFileExtensionException e) {
            throw ValidationException.withPath("fileExtension", e);
        } catch (IdempotentOperationId.Exception e) {
            throw ValidationException.withPath("operationId", e);
        } catch (GenerateUrlForUploadListingPhoto.Exception.WrongFileSize e) {
            throw ValidationException.withPath("fileSize", e);
        } catch (GenerateUrlForUploadListingPhoto.Exception.FailedGenerate e) {
            throw new Exception.FailedGenerate();
        } catch (UserId.WrongFormatException e) {
            throw ValidationException.withPath("hostId", e);
        } catch (ListingId.Exception.WrongFormat e) {
            throw ValidationException.withPath("listingId", e);
        }
    }
}
