package com.azat4dev.booking.listingsms.e2e.helpers;

import com.azat4dev.booking.listingsms.commands.domain.entities.ListingImpl;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listings;
import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.boot.test.context.TestComponent;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestComponent
@AllArgsConstructor
public class PhotoHelpers {

    private final Listings listings;
    private final GenerateUrlForUploadListingPhoto generateUrlForUploadListingPhoto;
    private final File testImageFile;

    private IdempotentOperationId anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return IdempotentOperationId.checkAndMakeFrom(UUID.randomUUID().toString());
    }

    private static void uploadFile(
        String url,
        Map<String, String> formFields,
        File file,
        String contentType
    ) throws Exception {

        // Upload an image using POST object with form-infrastructure.
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MultipartBody.FORM);

        for (Map.Entry<String, String> entry : formFields.entrySet()) {
            multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        multipartBuilder.addFormDataPart("Content-Type", contentType);

        // "file" must be added at last.
        multipartBuilder.addFormDataPart(
            "file", file.getName(), RequestBody.create(file, null)
        );

        Request request =
            new Request.Builder()
                .url(url)
                .post(multipartBuilder.build())
                .build();
        OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
        Response response = httpClient.newCall(request).execute();

        assertThat(response.isSuccessful()).isTrue();
    }

    public GeneratedUrlForUploadListingPhoto givenUploadedListingPhoto(
        UserId userId,
        ListingId listingId
    ) throws Exception {

        final var result = generateUrlForUploadListingPhoto.execute(
            anyIdempotentOperationId(),
            userId,
            listingId,
            PhotoFileExtension.dangerouslyMakeFrom("jpg"),
            (int) testImageFile.length()
        );

        uploadFile(
            result.formData().url().toString(),
            result.formData().formData(),
            testImageFile,
            "image/jpeg"
        );

        return result;
    }

    public ListingPhoto givenAddedPhoto(
        UserId userId,
        ListingId listingId
    ) throws Exception {


        final var result = givenUploadedListingPhoto(
            userId,
            listingId
        );

        final var listing = listings.getById(listingId)
            .orElseThrow(() -> new Exception("Listing not found"));

        final var photoId = listing.addNewPhoto(
            result.formData().bucketName(),
            result.formData().objectName()
        );

        listings.update(listing);

        return listing.getPhotos().stream()
            .filter(p -> p.getId().equals(photoId))
            .findFirst().orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    public void givenAllPhotosUploaded(
        UserId userId,
        ListingId listingId
    ) throws Exception {

        for (int i = 0; i < ListingImpl.MINIMUM_NUMBER_OF_PHOTOS; i++) {
            givenAddedPhoto(userId, listingId);
        }
    }
}
