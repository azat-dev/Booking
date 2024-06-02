package com.azat4dev.booking.listingsms.e2e;

import com.azat4dev.booking.listingsms.e2e.helpers.AccessTokenConfig;
import com.azat4dev.booking.listingsms.e2e.helpers.ApiHelpers;
import com.azat4dev.booking.listingsms.e2e.helpers.GenerateAccessToken;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsListingsPhotoApi;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsModificationsApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.client.model.GenerateUploadListingPhotoUrlRequestBody;
import com.azat4dev.booking.listingsms.helpers.MinioTests;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.github.javafaker.Faker;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER1;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {AccessTokenConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListingPhotosE2ETests implements PostgresTests, MinioTests {

    @MockBean(name = "listingsPhotoClient")
    MinioClient minioClient;

    @Autowired
    GenerateAccessToken generateAccessToken;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
    }

    @Test
    void test_addListing() {
        // Given
        final var userId = USER1;
        final var listingId = givenExistingListing(userId);

        // When
        final var result = apiClient(CommandsListingsPhotoApi.class, userId)
            .generateUploadListingPhotoUrl(
                listingId,
                new GenerateUploadListingPhotoUrlRequestBody()
                    .operationId(UUID.randomUUID())
                    .fileExtension("png")
                    .fileSize(1000)
                    .fileName("some_photo")
            );

        assertThat(result).isNotNull();
    }


    // Helpers

    static AddListingRequestBody anyRequestAddListing() {
        final var faker = Faker.instance();

        return new AddListingRequestBody()
            .operationId(UUID.randomUUID())
            .title(faker.book().title());
    }

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
}
