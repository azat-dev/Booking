package com.azat4dev.booking.listingsms;

import com.azat4dev.booking.listingsms.apiclient.DefaultApi;
import com.azat4dev.booking.listingsms.apiclient.api.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.apiclient.api.model.AddListingResponse;
import com.azat4dev.booking.listingsms.apiclient.api.model.GetListingPrivateDetailsResponse;
import com.azat4dev.booking.listingsms.apiclient.invoker.ApiException;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListingsMsApplicationTests implements PostgresTests {

    private final static Faker faker = Faker.instance();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void contextLoads() {
    }

    AddListingRequestBody anyRequestAddListing() {
        final var request = new AddListingRequestBody();
        request.setOperationId(UUID.randomUUID());
        request.setTitle(faker.book().title());

        return request;
    }

    @Test
    void test_addListing() throws ApiException {
        // Given
        final var requestAddListing = anyRequestAddListing();

        // When
        final var response = performAddNewListing(requestAddListing, "accessToken");

        // Then
        assertThat(response.getListingId()).isNotNull();

        final var details = performGetListingPrivateDetails(response.getListingId(), "accessToken");
        assertThat(details).isNotNull();
        assertThat(details.getListing().getId()).isEqualTo(response.getListingId());
    }

    // Helpers

    private String baseURL() {
        return "http://localhost:" + port;
    }

    DefaultApi apiClient() {
        final var api = new DefaultApi();
        api.setCustomBaseUrl(baseURL());

        return api;
    }

    AddListingResponse performAddNewListing(AddListingRequestBody requestBody, String accessToken) throws ApiException {

        final var api = apiClient();
        return api.addListing(requestBody);
    }

    GetListingPrivateDetailsResponse performGetListingPrivateDetails(UUID listingId, String accessToken) throws ApiException {

        final var api = apiClient();
        return api.getListingPrivateDetails(listingId);
    }
}
