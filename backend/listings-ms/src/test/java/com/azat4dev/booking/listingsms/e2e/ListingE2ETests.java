package com.azat4dev.booking.listingsms.e2e;

import com.azat4dev.booking.listingsms.e2e.helpers.AccessTokenConfig;
import com.azat4dev.booking.listingsms.e2e.helpers.ApiHelpers;
import com.azat4dev.booking.listingsms.e2e.helpers.GenerateAccessToken;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsModificationsApi;
import com.azat4dev.booking.listingsms.generated.client.api.QueriesPrivateApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.*;
import com.azat4dev.booking.listingsms.helpers.KafkaTests;
import com.azat4dev.booking.listingsms.helpers.MinioTests;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.github.javafaker.Faker;
import feign.FeignException;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.UUID;

import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER1;
import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {AccessTokenConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ListingE2ETests implements PostgresTests, MinioTests, KafkaTests {

    @MockBean(name = "listingsPhotoClient")
    MinioClient minioClient;

    @Autowired
    GenerateAccessToken generateAccessToken;

    @LocalServerPort
    private int port;

    static AddListingRequestBodyDTO anyRequestAddListing() {
        final var faker = Faker.instance();

        return new AddListingRequestBodyDTO()
            .operationId(UUID.randomUUID())
            .title(faker.book().title());
    }

    @Test
    void test_addListing_getListing() {
        // Given
        final var userId = USER1;

        // When
        final var listingId = givenExistingListing(userId);

        // Then
        assertThat(listingId).isNotNull();

        final var response = apiClient(QueriesPrivateApi.class, userId)
            .getListingPrivateDetails(listingId);

        assertThat(response.getListing().getId()).isEqualTo(listingId);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void test_addListing() throws UserId.WrongFormatException {
        // Given
        final var userId = UserId.checkAndMakeFrom(UUID.randomUUID().toString());

        // When
        givenExistingListing(userId);
    }

    @Test
    void test_getOnlyOwnListingDetails() throws UserId.WrongFormatException {

        // Given
        final var currentUserListing = givenExistingListing(USER1);
        final var anotherUserListing = givenExistingListing(USER2);

        // When
        final var exception = assertThrows(FeignException.Forbidden.class, () -> {
            apiClient(QueriesPrivateApi.class, USER1)
                .getListingPrivateDetails(anotherUserListing);
        });

        // Then
        assertThat(exception).isNotNull();
    }

    @Test
    void test_getOwnListings() {

        // Given
        final var currentUserListing1 = givenExistingListing(USER1);
        final var currentUserListing2 = givenExistingListing(USER1);

        final var anotherUserListing = givenExistingListing(USER2);

        // When
        final var ownListings = apiClient(QueriesPrivateApi.class, USER1)
            .getOwnListings();

        // Then
        assertThat(ownListings).hasSize(2);
        assertThat(ownListings).extracting("id")
            .contains(currentUserListing1, currentUserListing2);
    }

    public UUID givenExistingListing(UserId userId) {
        // Given
        final var requestAddListing = anyRequestAddListing();

        // When
        final var response = apiClient(CommandsModificationsApi.class, userId)
            .addListing(requestAddListing);

        return response.getListingId();
    }

    UpdateListingDetailsFieldsDTO anyFields() {
        final var f = Faker.instance();

        return new UpdateListingDetailsFieldsDTO()
            .title(f.book().title())
            .description(f.lorem().paragraph())
            .guestCapacity(
                new GuestsCapacityDTO()
                    .adults(f.number().numberBetween(1, 10))
                    .children(f.number().numberBetween(0, 5))
                    .infants(f.number().numberBetween(0, 3))
            ).propertyType(PropertyTypeDTO.BUNGALOW)
            .roomType(RoomTypeDTO.SHARED_ROOM)
            .address(
                new AddressDTO()
                    .country(f.address().country())
                    .city(f.address().city())
                    .street(f.address().streetAddress())
            );
    }

    @Test
    void test_updateListingDetails_givenExistingListing_thenUpdate() throws IOException {

        // Given
        final var userId = USER1;
        final var listingId = givenExistingListing(userId);

        // When
        final var updateData = anyFields();

        apiClient(CommandsModificationsApi.class, userId)
            .updateListingDetails(
                listingId,
                new UpdateListingDetailsRequestBodyDTO()
                    .operationId(UUID.randomUUID())
                    .fields(updateData)
            );

        // Then
        final var listingDetails = apiClient(QueriesPrivateApi.class, userId)
            .getListingPrivateDetails(listingId)
            .getListing();

        assertThat(listingDetails.getTitle()).isEqualTo(updateData.getTitle());
        assertThat(listingDetails.getDescription()).isEqualTo(updateData.getDescription());
        assertThat(listingDetails.getGuestCapacity()).isEqualTo(updateData.getGuestCapacity());
        assertThat(listingDetails.getPropertyType()).isEqualTo(updateData.getPropertyType());
        assertThat(listingDetails.getRoomType()).isEqualTo(updateData.getRoomType());
        assertThat(listingDetails.getAddress()).isEqualTo(updateData.getAddress());
    }

    private <T extends ApiClient.Api> T apiClient(Class<T> apiClass, UserId userId) {
        final var accessToken = generateAccessToken.execute(userId);
        return ApiHelpers.apiClient(apiClass, accessToken, port);
    }
}
