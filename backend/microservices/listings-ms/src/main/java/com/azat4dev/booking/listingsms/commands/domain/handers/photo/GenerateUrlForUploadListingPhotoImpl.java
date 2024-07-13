package com.azat4dev.booking.listingsms.commands.domain.handers.photo;

import com.azat4dev.booking.listingsms.commands.domain.events.FailedGenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Observed
@RequiredArgsConstructor
public class GenerateUrlForUploadListingPhotoImpl implements GenerateUrlForUploadListingPhoto {

    private final Duration expiresIn;
    private final GenerateObjectNameForListingPhoto generateObjectNameForListingPhoto;
    private final MediaObjectsBucket bucket;
    private final DomainEventsBus bus;

    @Override
    public GeneratedUrlForUploadListingPhoto execute(
        IdempotentOperationId operationId,
        UserId userId,
        ListingId listingId,
        PhotoFileExtension fileExtension,
        int fileSize
    ) throws ValidationException, Exception.FailedGenerate, Exception.WrongFileSize {

        final Runnable publishFailedEvent = () -> {
            bus.publish(new FailedGenerateUrlForUploadListingPhoto(
                operationId,
                userId,
                listingId,
                fileExtension,
                fileSize
            ));
        };

        if (fileSize < MIN_FILE_SIZE || fileSize > MAX_FILE_SIZE) {
            publishFailedEvent.run();
            throw new Exception.WrongFileSize();
        }

        try {

            final var objectName = generateObjectNameForListingPhoto.execute(userId, listingId, fileExtension);

            final var formData = bucket.generateUploadFormData(
                objectName,
                expiresIn,
                new MediaObjectsBucket.Policy(
                    Optional.of(new MediaObjectsBucket.FileSizeRange(0, MAX_FILE_SIZE)),
                    new MediaObjectsBucket.Condition[]{
                        new MediaObjectsBucket.Condition(
                            "Content-Type",
                            MediaObjectsBucket.ConditionType.STARTS_WITH,
                            "image/"
                        )
                    }
                )
            );

            final var event = new GeneratedUrlForUploadListingPhoto(userId, listingId, formData);
            bus.publish(event);
            return event;

        } catch (Throwable e) {

            publishFailedEvent.run();
            throw new Exception.FailedGenerate();
        }
    }
}
