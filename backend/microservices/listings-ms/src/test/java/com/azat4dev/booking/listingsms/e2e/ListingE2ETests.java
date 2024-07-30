package com.azat4dev.booking.listingsms.e2e;

import com.azat4dev.booking.listingsms.e2e.helpers.*;
import com.azat4dev.booking.listingsms.generated.client.api.CommandsModificationsApi;
import com.azat4dev.booking.listingsms.generated.client.api.QueriesPrivateApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.*;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;
import java.util.function.Function;

import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER1;
import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({TestHelpersConfig.class})
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/db/drop-schema.sql", "/db/schema.sql"})
@AutoConfigureObservability /* The order matters for: ./mvnw test */
class ListingE2ETests {

    @Autowired
    GenerateAccessToken generateAccessToken;

    @Autowired
    ListingHelpers listingHelpers;

    @LocalServerPort
    private int port;

    static AddListingRequestBodyDTO anyRequestAddListing() {
        final var faker = new Faker();

        return new AddListingRequestBodyDTO()
            .operationId(UUID.randomUUID())
            .title(faker.book().title());
    }

    @Test
    void test_addListing_and_getListing() {
        // Given
        final var userId = USER1;
        final var requestAddListing = anyRequestAddListing();

        // When
        final var addListingResponse = apiClient(CommandsModificationsApi::new, userId)
            .addListing(requestAddListing);

        final var listingId = addListingResponse.getListingId();

        // Then
        assertThat(listingId).isNotNull();

        final var getListingResponse = apiClient(QueriesPrivateApi::new, userId)
            .getListingPrivateDetails(listingId);

        assertThat(getListingResponse.getListing().getId()).isEqualTo(listingId);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void test_getOnlyOwnListingDetails() throws Exception {

        // Given
        final var currentUserListing = listingHelpers.givenExistingListing(USER1);
        final var anotherUserListing = listingHelpers.givenExistingListing(USER2);

        // When
        final var exception = assertThrows(RestClientResponseException.class, () -> {
            apiClient(QueriesPrivateApi::new, USER1)
                .getListingPrivateDetails(anotherUserListing.getId().getValue());
        });

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getStatusCode())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void test_getOwnListings() throws Exception {

        // Given
        final var currentUserListing1 = listingHelpers.givenExistingListing(USER1);
        final var currentUserListing2 = listingHelpers.givenExistingListing(USER1);

        final var anotherUserListing = listingHelpers.givenExistingListing(USER2);

        // When
        final var ownListings = apiClient(QueriesPrivateApi::new, USER1)
            .getOwnListings();

        // Then
        assertThat(ownListings).hasSize(2);
        assertThat(ownListings).extracting("id")
            .contains(currentUserListing1.getId().getValue(), currentUserListing2.getId().getValue());
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
    void test_publishListing_givenDraftListing_thenRejectPublishing() throws Exception {

        // Given
        final var userId = USER1;
        final var listingId = listingHelpers.givenExistingListing(userId)
            .getId().getValue();

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


    @Test
    void test_publishListing_givenListingReadyForPublishing_thenPublish() throws Exception {

        // Given
        final var userId = USER1;
        final var listing = listingHelpers.givenListingReadyForPublishing(userId);
        final var listingId = listing.getId().getValue();

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
    void test_updateListingDetails_givenExistingListing_thenUpdate() throws Exception {

        // Given
        final var userId = USER1;
        final var listingId = listingHelpers.givenExistingListing(userId)
            .getId().getValue();

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
