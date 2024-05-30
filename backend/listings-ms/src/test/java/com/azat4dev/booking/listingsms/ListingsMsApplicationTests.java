package com.azat4dev.booking.listingsms;

import com.azat4dev.booking.listingsms.generated.client.api.CommandsModificationsApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@EnableFeignClients
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListingsMsApplicationTests implements PostgresTests {

    private final static Faker faker = Faker.instance();

    @Test
    void contextLoads() {
    }

    AddListingRequestBody anyRequestAddListing() {
        return new AddListingRequestBody()
            .operationId(UUID.randomUUID())
            .title(faker.book().title());
    }

    @Test
    void test_addListing() {
        // Given
        final var requestAddListing = anyRequestAddListing();

        // When
        final var body = anonymousClient(CommandsModificationsApi.class)
            .addListing(requestAddListing);

        // Then
        assertThat(body.getListingId()).isNotNull();

        final var detailsResponse = anony.getListingPrivateDetails(body.getListingId());
        final var details = detailsResponse.getBody();

        assertThat(details).isNotNull();
        assertThat(details.getListing().getId()).isEqualTo(body.getListingId());
    }

    // Helpers

    private <T extends ApiClient.Api> T anonymousClient(Class<T> apiClass) {
        final var api = new ApiClient();
        api.setBasePath("http://localhost:" + port);
        return api.buildClient(apiClass);
    }

    private <T extends ApiClient.Api> T apiClient(String accessToken, Class<T> apiClass) {
        final var api = new ApiClient("BearerAuth");
        api.setBearerToken(accessToken);
        api.setBasePath("http://localhost:" + port);
        return api.buildClient(apiClass);
    }
}
