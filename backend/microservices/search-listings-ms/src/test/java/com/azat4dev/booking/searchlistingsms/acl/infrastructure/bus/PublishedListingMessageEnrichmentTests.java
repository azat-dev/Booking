package com.azat4dev.booking.searchlistingsms.acl.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.generated.api.bus.Channels;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.listingsqueries.responses.getlistingpublicdetailsbyid.GetListingPublicDetailsByIdParamsDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.listingsqueries.responses.getlistingpublicdetailsbyid.GetListingPublicDetailsByIdResponseDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.listingsqueries.responses.getlistingpublicdetailsbyid.ListingPublicDetailsDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.internallistingeventsstream.ListingPublishedDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.internallistingeventsstream.WaitingInfoForPublishedListingDTO;
import com.azat4dev.booking.searchlistingsms.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.infrastructure.bus.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.azat4dev.booking.searchlistingsms.helpers.Helpers.waitForValue;

@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/db/drop-schema.sql", "/db/schema.sql"})
@AutoConfigureObservability /* The order matters for: ./mvnw test */
public class PublishedListingMessageEnrichmentTests {

    @Autowired
    AtomicReference<ListingPublishedDTO> receivedListingPublished;

    @Autowired
    private MessageBus messageBus;

    private UUID anyListingId() {
        return UUID.randomUUID();
    }

    com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.externallistingeventsstream.ListingPublishedDTO anyListingPublished() {
        return com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.externallistingeventsstream.ListingPublishedDTO.newBuilder()
            .setListingId(anyListingId())
            .setPublishedAt(LocalDateTime.now().toString())
            .build();
    }

    @Test
    void test_givenWaitingForListingDetails_whenReceivedResponse_thenPublishEnrichedListingPublishedMessage() {

        // Given
        final var listingId = anyListingId();
        final var listingPublished = anyListingPublished();

        final var requestId = UUID.randomUUID();

        messageBus.publish(
            Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
            Optional.of(requestId.toString()),
            MessageBus.Data.with(
                UUID.randomUUID().toString(),
                "WaitingInfoForPublishedListing",
                WaitingInfoForPublishedListingDTO.newBuilder()
                    .setListingId(listingId)
                    .setRequestId(requestId)
                    .build()
            )
        );

        messageBus.publish(
            Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.of(requestId.toString()),
            MessageBus.Data.with(
                UUID.randomUUID().toString(),
                "ListingPublished",
                GetListingPublicDetailsByIdResponseDTO.newBuilder()
                    .setParams(
                        GetListingPublicDetailsByIdParamsDTO.newBuilder()
                            .setListingId(listingId)
                            .build()
                    )
                    .setData(
                        ListingPublicDetailsDTO.newBuilder()
                            .build()
                    )
            )
        );

        // Then
        waitForValue(receivedListingPublished, Duration.ofSeconds(3));
    }

    @TestConfiguration
    public static class Config {

        public static final String JOINED_MESSAGES_TOPIC = "topic";

        @Bean
        AtomicReference<ListingPublishedDTO> receivedListingPublished() {
            return new AtomicReference<>();
        }


        @Bean
        NewMessageListenerForChannel outputMessageListener(AtomicReference<ListingPublishedDTO> receivedListingPublished) {
            return new NewMessageListenerForChannel(
                Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
                new MessageListener() {

                    @Override
                    public Optional<Set<String>> messageTypes() {
                        return Optional.of(Set.of("ListingPublished"));
                    }

                    @Override
                    public void consume(Message message) {
                        receivedListingPublished.set(message.payloadAs(ListingPublishedDTO.class));
                    }
                }
            );
        }

//        @Bean
//        StreamFactoryForTopic streamFactoryForTopic() {
//            return new StreamFactoryForTopic(
//                JOINED_MESSAGES_TOPIC,
//                new StreamFactory() {
//                    @Override
//                    public KStream<String, Message> make(StreamsBuilder builder) {
//
//                        final var table = builder.stream(Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue())
//                            .filter((key, value) -> value instanceof WaitingInfoForPublishedListingDTO)
//                            .toTable();
//
//                        builder.stream(Channels.RECEIVE_GET_PUBLIC_LISTING_PUBLIC_DETAILS_BY_ID_FOR_PUBLISHED_LISTING.getValue())
//                            .selectKey()
//                    }
//                }
//            );
//        }
    }
}
