package com.azat4dev.booking.listingsms.e2e;

import com.azat4dev.booking.listingsms.e2e.helpers.AccessTokenConfig;
import com.azat4dev.booking.listingsms.e2e.helpers.ApiHelpers;
import com.azat4dev.booking.listingsms.e2e.helpers.GenerateAccessToken;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsListingsPhotoApi;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsModificationsApi;
import com.azat4dev.booking.listingsms.generated.client.api.QueriesPrivateApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.*;
import com.azat4dev.booking.listingsms.helpers.KafkaTests;
import com.azat4dev.booking.listingsms.helpers.MinioTests;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.github.javafaker.Faker;
import io.minio.MinioClient;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER1;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {AccessTokenConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ListingPhotosE2ETests implements PostgresTests, MinioTests, KafkaTests {

    @MockBean(name = "listingsPhotoClient")
    MinioClient minioClient;

    @Autowired
    GenerateAccessToken generateAccessToken;

    @LocalServerPort
    private int port;

    @Value("classpath:test_image.jpg")
    private Resource testImageFile;

    static AddListingRequestBodyDTO anyRequestAddListing() {
        final var faker = Faker.instance();

        return new AddListingRequestBodyDTO()
            .operationId(UUID.randomUUID())
            .title(faker.book().title());
    }

    @Test
    void contextLoads() {
    }

    @Test
    void test_addGenerateUploadUrl() throws IOException {
        // Given
        final var userId = USER1;
        final var listingId = givenExistingListing(userId);

        // When
        final var result = givenUploadedListingPhoto(userId, listingId);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void test_addPhotoToListing() throws IOException {

        // Given
        final var userId = USER1;
        final var listingId = givenExistingListing(userId);

        final var result = givenUploadedListingPhoto(userId, listingId);

        final var response = apiClient(CommandsListingsPhotoApi.class, userId)
            .addPhotoToListing(
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

        // Then
        final var listingDetails = apiClient(QueriesPrivateApi.class, userId)
            .getListingPrivateDetails(listingId)
            .getListing();


        assertThat(listingDetails.getPhotos().size()).isEqualTo(1);
    }

    private GenerateUploadListingPhotoUrlResponseBodyDTO givenUploadedListingPhoto(
        UserId userId,
        UUID listingId
    ) throws IOException {

        // Given
        final var file = testImageFile.getFile();

        // When
        final var result = apiClient(CommandsListingsPhotoApi.class, userId)
            .generateUploadListingPhotoUrl(
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

    private void uploadFile(
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

    // Helpers

    public UUID givenExistingListing(UserId userId) {
        // Given
        final var requestAddListing = anyRequestAddListing();

        // When
        final var response = apiClient(CommandsModificationsApi.class, userId)
            .addListing(requestAddListing);

        return response.getListingId();
    }

    private <T extends ApiClient.Api> T apiClient(Class<T> apiClass, UserId userId) {
        final var accessToken = generateAccessToken.execute(userId);
        return ApiHelpers.apiClient(apiClass, accessToken, port);
    }

    @TestConfiguration
    static class Config {

        @Bean
        File testImageFile() {
            return new File("src/test/resources/test-image.png");
        }
    }
}
