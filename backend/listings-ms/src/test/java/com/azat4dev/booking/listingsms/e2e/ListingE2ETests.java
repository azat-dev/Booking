package com.azat4dev.booking.listingsms.e2e;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.e2e.helpers.AccessTokenConfig;
import com.azat4dev.booking.listingsms.e2e.helpers.ApiHelpers;
import com.azat4dev.booking.listingsms.e2e.helpers.EnableTestcontainers;
import com.azat4dev.booking.listingsms.e2e.helpers.GenerateAccessToken;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsListingsPhotoApi;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsModificationsApi;
import com.azat4dev.booking.listingsms.generated.client.api.QueriesPrivateApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.*;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import static com.azat4dev.booking.listingsms.e2e.helpers.PhotoHelpers.givenAllPhotosUploaded;
import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER1;
import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableTestcontainers(classes = {AccessTokenConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/db/drop-schema.sql", "/db/schema.sql"})
class ListingE2ETests {

    @Autowired
    GenerateAccessToken generateAccessToken;

    @Value("classpath:test_image.jpg")
    private Resource testImageFile;

    @LocalServerPort
    private int port;

    static AddListingRequestBodyDTO anyRequestAddListing() {
        final var faker = new Faker();

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

        final var response = apiClient(QueriesPrivateApi::new, userId)
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
        final var exception = assertThrows(RestClientResponseException.class, () -> {
            apiClient(QueriesPrivateApi::new, USER1)
                .getListingPrivateDetails(anotherUserListing);
        });

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getStatusCode())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void test_getOwnListings() {

        // Given
        final var currentUserListing1 = givenExistingListing(USER1);
        final var currentUserListing2 = givenExistingListing(USER1);

        final var anotherUserListing = givenExistingListing(USER2);

        // When
        final var ownListings = apiClient(QueriesPrivateApi::new, USER1)
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
        final var response = apiClient(CommandsModificationsApi::new, userId)
            .addListing(requestAddListing);

        return response.getListingId();
    }

    UpdateListingDetailsFieldsDTO anyFields() {
        final var f = new Faker();

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
    void test_publishListing_givenDraftListing_thenRejectPublishing() {

        // Given
        final var userId = USER1;
        final var listingId = givenExistingListing(userId);

        // When
        final var exception = assertThrows(RestClientResponseException.class, () -> {
            apiClient(CommandsModificationsApi::new, userId)
                .publishListing(listingId);
        });

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getStatusCode())
            .isEqualTo(HttpStatus.CONFLICT);
    }

    UUID givenListingReadyForPublishing(UserId userId) throws IOException {
        // Given
        final var listingId = givenExistingListing(userId);

        apiClient(CommandsModificationsApi::new, userId)
            .updateListingDetails(
                listingId,
                new UpdateListingDetailsRequestBodyDTO()
                    .operationId(UUID.randomUUID())
                    .fields(anyFields())
            );

        givenAllPhotosUploaded(
            listingId,
            apiClient(CommandsListingsPhotoApi::new, userId),
            testImageFile
        );

        final var listingDetails = apiClient(QueriesPrivateApi::new, userId)
            .getListingPrivateDetails(listingId);

        assertThat(listingDetails.getListing().getPhotos().size())
            .isEqualTo(Listing.MINIMUM_NUMBER_OF_PHOTOS);

        return listingId;
    }

    @Test
    void test_publishListing_givenListingReadyForPublishing_thenPublish() throws IOException {

        // Given
        final var userId = USER1;
        final var listingId = givenListingReadyForPublishing(userId);

        apiClient(CommandsModificationsApi::new, userId)
            .updateListingDetails(
                listingId,
                new UpdateListingDetailsRequestBodyDTO()
                    .operationId(UUID.randomUUID())
                    .fields(anyFields())
            );

        // When
        final var result = apiClient(CommandsModificationsApi::new, userId)
            .publishListingWithHttpInfo(listingId);

        // Then
        assertThat(result.getStatusCode())
            .isEqualTo(HttpStatus.NO_CONTENT);

        final var listingDetails = apiClient(QueriesPrivateApi::new, userId)
            .getListingPrivateDetails(listingId)
            .getListing();

        assertThat(listingDetails.getStatus()).isEqualTo(ListingStatusDTO.PUBLISHED);
    }

    @Test
    void test_updateListingDetails_givenExistingListing_thenUpdate() throws IOException {

        // Given
        final var userId = USER1;
        final var listingId = givenExistingListing(userId);

        // When
        final var updateData = anyFields();

        apiClient(CommandsModificationsApi::new, userId)
            .updateListingDetails(
                listingId,
                new UpdateListingDetailsRequestBodyDTO()
                    .operationId(UUID.randomUUID())
                    .fields(updateData)
            );

        // Then
        final var listingDetails = apiClient(QueriesPrivateApi::new, userId)
            .getListingPrivateDetails(listingId)
            .getListing();

        assertThat(listingDetails.getTitle()).isEqualTo(updateData.getTitle());
        assertThat(listingDetails.getDescription()).isEqualTo(updateData.getDescription());
        assertThat(listingDetails.getGuestCapacity()).isEqualTo(updateData.getGuestCapacity());
        assertThat(listingDetails.getPropertyType()).isEqualTo(updateData.getPropertyType());
        assertThat(listingDetails.getRoomType()).isEqualTo(updateData.getRoomType());
        assertThat(listingDetails.getAddress()).isEqualTo(updateData.getAddress());
    }

    private <T> T apiClient(Function<ApiClient, T> factory, UserId userId) {

        final var token = generateAccessToken.execute(userId);
        return ApiHelpers.apiClient(factory, token, port);
    }
}
