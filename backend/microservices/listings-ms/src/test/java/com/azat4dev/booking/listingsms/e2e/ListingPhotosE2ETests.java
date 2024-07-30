package com.azat4dev.booking.listingsms.e2e;

import com.azat4dev.booking.listingsms.e2e.helpers.*;
import com.azat4dev.booking.listingsms.generated.client.api.QueriesPrivateApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingRequestBodyDTO;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import io.minio.MinioClient;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;
import java.util.function.Function;

import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER1;
import static org.assertj.core.api.Assertions.assertThat;

@Import(TestHelpersConfig.class)
@EnableTestcontainers(classes = {AccessTokenConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/db/drop-schema.sql", "/db/schema.sql"})
class ListingPhotosE2ETests {

    @MockBean(name = "listingsPhotoClient")
    MinioClient minioClient;

    @Autowired
    GenerateAccessToken generateAccessToken;

    @Autowired
    ListingHelpers listingHelpers;

    @Autowired
    PhotoHelpers photoHelpers;

    @LocalServerPort
    private int port;

    @Value("classpath:test_image.jpg")
    private Resource testImageFile;

    static AddListingRequestBodyDTO anyRequestAddListing() {
        final var faker = new Faker();

        return new AddListingRequestBodyDTO()
            .operationId(UUID.randomUUID())
            .title(faker.book().title());
    }

    @Test
    void contextLoads() {
    }

    @Test
    void test_addGenerateUploadUrl() throws Exception {
        // Given
        final var userId = USER1;
        final var listingId = listingHelpers.givenExistingListing(userId)
            .getId();

        // When
        final var result = photoHelpers.givenUploadedListingPhoto(
            userId,
            listingId
        );

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void test_addPhotoToListing() throws Exception {

        // Given
        final var userId = USER1;
        final var listingId = listingHelpers.givenExistingListing(userId)
            .getId();

        final var ignored = photoHelpers.givenUploadedListingPhoto(
            userId,
            listingId
        );

        photoHelpers.givenAddedPhoto(
            userId,
            listingId
        );

        photoHelpers.givenAddedPhoto(
            userId,
            listingId
        );

        // Then
        final var listingDetails = apiClient(QueriesPrivateApi::new, userId)
            .getListingPrivateDetails(listingId.getValue())
            .getListing();

        assertThat(listingDetails.getPhotos().size()).isEqualTo(2);
    }

    // Helpers

    private <T> T apiClient(Function<ApiClient, T> factory, UserId userId) {

        final var token = generateAccessToken.execute(userId);
        return ApiHelpers.apiClient(factory, token, port);
    }
}
