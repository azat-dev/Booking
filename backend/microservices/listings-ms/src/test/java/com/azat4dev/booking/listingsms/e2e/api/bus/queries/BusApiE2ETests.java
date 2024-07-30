package com.azat4dev.booking.listingsms.e2e.api.bus.queries;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listings;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.listingsms.e2e.helpers.EnableTestcontainers;
import com.azat4dev.booking.listingsms.e2e.helpers.ListingHelpers;
import com.azat4dev.booking.listingsms.e2e.helpers.TestHelpersConfig;
import com.azat4dev.booking.listingsms.generated.api.bus.Channels;
import com.azat4dev.booking.listingsms.generated.events.dto.*;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.azat4dev.booking.listingsms.e2e.helpers.UsersHelpers.USER1;
import static org.assertj.core.api.Assertions.assertThat;

@Import(TestHelpersConfig.class)
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/db/drop-schema.sql", "/db/schema.sql"})
@AutoConfigureObservability /* The order matters for: ./mvnw test */
class BusApiE2ETests {

    @Autowired
    private MessageBus<String> messageBus;

    @Autowired
    Listings listings;

    @Autowired
    MakeNewListingId makeNewListingId;

    @Autowired
    ListingHelpers helpers;


    @Test
    void test_getPublicListingData_givenExistingNotPublishedListing_thenPublishForbiddenError() throws Exception {

        // Given
        final var eventId = UUID.randomUUID().toString();
        final var existingListing = helpers.givenExistingListing(USER1);

        final var completed = new AtomicBoolean(false);

        final var listener = messageBus.listen(
            Channels.QUERIES_RESPONSES__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            (message) -> {
                if (message.correlationId().isEmpty()) {
                    return;
                }

                final var receivedCorrelationId = message.correlationId().get();
                if (!receivedCorrelationId.equals(eventId)) {
                    return;
                }

                final var payload = message.payload(FailedGetListingPublicDetailsByIdDTO.class);
                assertThat(payload.getError().getCode())
                    .isEqualTo(FailedGetListingPublicDetailsByIdErrorCodeDTO.FORBIDDEN);
                completed.set(true);
            }
        );

        // When
        messageBus.publish(
            Channels.QUERIES_REQUESTS__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.empty(),
            Optional.empty(),
            eventId,
            "GetListingPublicDetailsById",
            GetListingPublicDetailsByIdDTO.builder()
                .params(
                    GetListingPublicDetailsByIdParamsDTO.builder()
                        .listingId(existingListing.getId().getValue())
                        .build()
                ).build()
        );

        // Then
        Awaitility.await()
            .atMost(Duration.of(10, ChronoUnit.SECONDS))
            .untilTrue(completed);
        listener.close();
    }

    @Test
    void test_getPublicListingData_givenExistingListing_thenPublishResponse() throws Exception {

        // Given
        final var eventId = UUID.randomUUID().toString();
        final var existingListing = helpers.givenPublishedListing(USER1);
        final var listingId = existingListing.getId();

        final var completed = new AtomicBoolean(false);

        final var listener = messageBus.listen(
            Channels.QUERIES_RESPONSES__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            (message) -> {
                if (message.correlationId().isEmpty()) {
                    return;
                }

                final var receivedCorrelationId = message.correlationId().get();
                if (!receivedCorrelationId.equals(eventId)) {
                    return;
                }

                assertThat(message.payload()).isInstanceOf(GetListingPublicDetailsByIdResponseDTO.class);
//                assertThat(message.payload()).isEqualTo()
                completed.set(true);
            }
        );

        // When
        messageBus.publish(
            Channels.QUERIES_REQUESTS__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.empty(),
            Optional.empty(),
            eventId,
            "GetListingPublicDetailsById",
            GetListingPublicDetailsByIdDTO.builder()
                .params(
                    GetListingPublicDetailsByIdParamsDTO.builder()
                        .listingId(listingId.getValue())
                        .build()
                ).build()
        );

        // Then

        Awaitility.await()
            .atMost(Duration.of(10, ChronoUnit.SECONDS))
            .untilTrue(completed);
        listener.close();
    }

    @Test
    void test_getPublicListingData_givenNotExistingListing_thenPublishError() throws IOException {

        // Given
        final var eventId = UUID.randomUUID().toString();
        final var notExistingListingId = UUID.randomUUID();

        final var params = GetListingPublicDetailsByIdParamsDTO.builder()
            .listingId(notExistingListingId)
            .build();

        // When
        messageBus.publish(
            Channels.QUERIES_REQUESTS__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.empty(),
            Optional.empty(),
            eventId,
            "GetListingPublicDetailsById",
            GetListingPublicDetailsByIdDTO.builder()
                .params(
                    params
                ).build()
        );

        // Then
        final var completed = new AtomicBoolean(false);
        final var listener = messageBus.listen(
            Channels.QUERIES_RESPONSES__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            (message) -> {
                if (message.correlationId().isEmpty()) {
                    return;
                }

                final var receivedCorrelationId = message.correlationId().get();
                if (!receivedCorrelationId.equals(eventId)) {
                    return;
                }

                assertThat(message.payload()).isInstanceOf(FailedGetListingPublicDetailsByIdDTO.class);
                final var expectedResponse = FailedGetListingPublicDetailsByIdDTO.builder()
                    .error(
                        new FailedGetListingPublicDetailsByIdErrorDTO(
                            FailedGetListingPublicDetailsByIdErrorCodeDTO.NOT_FOUND,
                            "Listing not found: id=" + notExistingListingId
                        )
                    )
                    .params(params)
                    .build();
                assertThat(message.payload()).isEqualTo(expectedResponse);
                completed.set(true);
            }
        );

        Awaitility.await()
            .atMost(Duration.of(100, ChronoUnit.SECONDS))
            .untilTrue(completed);
        listener.close();
    }
}
