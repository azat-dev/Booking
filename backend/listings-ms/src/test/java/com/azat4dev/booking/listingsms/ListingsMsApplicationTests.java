package com.azat4dev.booking.listingsms;

import com.azat4dev.booking.listingsms.generated.client.api.CommandsApiClient;
import com.azat4dev.booking.listingsms.generated.client.api.QueriesApi;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@EnableFeignClients
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListingsMsApplicationTests implements PostgresTests {

    private final static Faker faker = Faker.instance();

    @Autowired
    CommandsApiClient commandsApiClient;

    @Autowired
    QueriesApi queriesApiClient;

    @DynamicPropertySource
    protected static void feignProperties(DynamicPropertyRegistry registry) {
        registry.add("commands.url", () -> "http://localhost:${local.server.port}");
        registry.add("queries.url", () -> "http://localhost:${local.server.port}");
    }

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
        final var response = commandsApiClient.addListing(requestAddListing);

        // Then
        final var body = response.getBody();
        assertThat(body.getListingId()).isNotNull();

        final var detailsResponse = queriesApiClient.getListingPrivateDetails(body.getListingId());
        final var details = detailsResponse.getBody();

        assertThat(details).isNotNull();
        assertThat(details.getListing().getId()).isEqualTo(body.getListingId());
    }
}
