package com.azat4dev.booking.listingsms.e2e.helpers;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsListingsPhotoApi;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingPhotoRequestBodyDTO;
import com.azat4dev.booking.listingsms.generated.client.model.GenerateUploadListingPhotoUrlRequestBodyDTO;
import com.azat4dev.booking.listingsms.generated.client.model.GenerateUploadListingPhotoUrlResponseBodyDTO;
import com.azat4dev.booking.listingsms.generated.client.model.UploadedFileDataDTO;
import okhttp3.*;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PhotoHelpers {

    public static void uploadFile(
        String url,
        String objectName,
        Map<String, String> formFields,
        File file,
        String contentType
    ) throws IOException {

        // Upload an image using POST object with form-data.
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

        final var responseBody = response.body().string();
        assertThat(response.isSuccessful()).isTrue();
    }

    public static GenerateUploadListingPhotoUrlResponseBodyDTO givenUploadedListingPhoto(
        UUID listingId,
        CommandsListingsPhotoApi apiClient,
        Resource testImageFile
    ) throws IOException {

        // Given
        final var file = testImageFile.getFile();

        // When
        final var result = apiClient.generateUploadListingPhotoUrl(
            listingId,
            new GenerateUploadListingPhotoUrlRequestBodyDTO()
                .operationId(UUID.randomUUID())
                .fileExtension("jpg")
                .fileSize((int) file.length())
                .fileName("test-image")
        );

        // Then
        assertThat(result).isNotNull();
        uploadFile(
            result.getObjectPath().getUrl(),
            result.getObjectPath().getObjectName(),
            result.getFormData(),
            file,
            "image/jpeg"
        );
        return result;
    }

    public static void givenAddedPhoto(
        UUID listingId,
        CommandsListingsPhotoApi apiClient,
        Resource testImageFile
    ) throws IOException {

        final var result = givenUploadedListingPhoto(
            listingId,
            apiClient,
            testImageFile
        );

        apiClient.addPhotoToListing(
            listingId,
            new AddListingPhotoRequestBodyDTO()
                .operationId(UUID.randomUUID())
                .uploadedFile(
                    new UploadedFileDataDTO()
                        .url(result.getObjectPath().getUrl())
                        .bucketName(result.getObjectPath().getBucketName())
                        .objectName(result.getObjectPath().getObjectName())
                )
        );
    }

    public static void givenAllPhotosUploaded(
        UUID listingId,
        CommandsListingsPhotoApi apiClient,
        Resource testImageFile
    ) throws IOException {

        for (int i = 0; i < Listing.MINIMUM_NUMBER_OF_PHOTOS; i++) {
            givenAddedPhoto(listingId, apiClient, testImageFile);
        }
    }
}
