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
import java.util.concurrent.atomic.AtomicReference;

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
        final var replyChannel = UUID.randomUUID().toString();

        // When
        final var promise = publish(
            Channels.QUERIES_REQUESTS__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.empty(),
            eventId,
            "GetListingPublicDetailsById",
            GetListingPublicDetailsByIdDTO.builder()
                .params(
                    GetListingPublicDetailsByIdParamsDTO.builder()
                        .listingId(existingListing.getId().getValue())
                        .build()
                ).build(),
            Optional.of(replyChannel)
        );


        final var response = (FailedGetListingPublicDetailsByIdDTO) promise.waitFor(
            "FailedGetListingPublicDetailsById",
            10000
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getError().getCode())
            .isEqualTo(FailedGetListingPublicDetailsByIdErrorCodeDTO.FORBIDDEN);
    }

    @Test
    void test_getPublicListingData_givenExistingListing_thenPublishResponse() throws Exception {

        // Given
        final var eventId = UUID.randomUUID().toString();
        final var existingListing = helpers.givenPublishedListing(USER1);
        final var listingId = existingListing.getId();
        final var replyChannel = UUID.randomUUID().toString();

        // When
        final var promise = publish(
            Channels.QUERIES_REQUESTS__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.empty(),
            eventId,
            "GetListingPublicDetailsById",
            GetListingPublicDetailsByIdDTO.builder()
                .params(
                    GetListingPublicDetailsByIdParamsDTO.builder()
                        .listingId(listingId.getValue())
                        .build()
                ).build(),
            Optional.of(replyChannel)
        );

        final var response = (GetListingPublicDetailsByIdResponseDTO) promise.waitFor(
            "GetListingPublicDetailsByIdResponse",
            10000
        );

        // Then
        assertThat(response).isNotNull();

        final var responseData = response.getData();
        assertThat(responseData.getId()).isEqualTo(listingId.getValue());
    }

    @Test
    void test_getPublicListingData_givenNotExistingListing_thenPublishError() throws IOException {

        // Given
        final var eventId = UUID.randomUUID().toString();
        final var notExistingListingId = UUID.randomUUID();

        final var params = GetListingPublicDetailsByIdParamsDTO.builder()
            .listingId(notExistingListingId)
            .build();

        final var replyChannel = UUID.randomUUID().toString();

        // When
        final var promise = publish(
            Channels.QUERIES_REQUESTS__GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.empty(),
            eventId,
            "GetListingPublicDetailsById",
            GetListingPublicDetailsByIdDTO.builder()
                .params(
                    params
                ).build(),
            Optional.of(replyChannel)
        );

        final var response = (FailedGetListingPublicDetailsByIdDTO) promise.waitFor(
            "FailedGetListingPublicDetailsById",
            10000
        );

        // Then
        assertThat(response).isInstanceOf(FailedGetListingPublicDetailsByIdDTO.class);
        assertThat(response.getError().getCode())
            .isEqualTo(FailedGetListingPublicDetailsByIdErrorCodeDTO.NOT_FOUND);
    }

    @FunctionalInterface
    interface MessagePromise {

        Object waitFor(
            String responseMessageType,
            long waitMs
        );
    }

    private MessagePromise publish(
        String channel,
        Optional<String> partitionKey,
        String messageId,
        String messageType,
        Object messageData,
        Optional<String> replyTo
    ) {

        messageBus.publish(
            channel,
            partitionKey,
            messageId,
            messageType,
            messageData,
            replyTo,
            Optional.of(messageId)
        );

        return (responseMessageType, waitMs) -> {

            final var completed = new AtomicBoolean(false);
            AtomicReference<Object> result = new AtomicReference<>();

            final var listener = messageBus.listen(
                replyTo.get(),
                (message) -> {
                    if (message.correlationId().isEmpty()) {
                        return;
                    }

                    final var receivedCorrelationId = message.correlationId().get();
                    if (!receivedCorrelationId.equals(messageId)) {
                        return;
                    }

                    if (!message.messageType().equals(responseMessageType)) {
                        return;
                    }

                    result.set(message.payload());
                    completed.set(true);
                }
            );

            Awaitility.await()
                .atMost(Duration.of(waitMs, ChronoUnit.MILLIS))
                .untilTrue(completed);

            try {
                listener.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result.get();
        };
    }
}
