package com.azat4dev.booking.searchlistingsms.acl.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.generated.api.bus.Channels;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.*;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.ListingPublishedDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.WaitingInfoForPublishedListingDTO;
import com.azat4dev.booking.searchlistingsms.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListener;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenerForChannel;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus.KafkaStreamsPublishedListingEnrichmentConfig.WAITINGS_STORE_NAME;
import static com.azat4dev.booking.searchlistingsms.helpers.Helpers.waitForValue;
import static org.assertj.core.api.Assertions.assertThat;

@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/db/drop-schema.sql", "/db/schema.sql"})
@AutoConfigureObservability /* The order matters for: ./mvnw test */
public class PublishedListingMessageEnrichmentTests {

    @Autowired
    AtomicReference<ListingPublishedDTO> receivedListingPublished;

    @Autowired
    private MessageBus messageBus;

    @Autowired
    private KafkaStreamsInteractiveQueryService interactiveQueryService;

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Test
    void test_givenWaitingForListingDetails_whenReceivedResponse_thenPublishEnrichedListingPublishedMessageAndDeleteRecordFromWaitingsTable() throws InterruptedException {

        // Given
        final var waiting = anyWaiting();
        final var listingId = waiting.getListingId();
        final var response = anyResponse(listingId);

        final var requestId = UUID.randomUUID();

        // When
        messageBus.publish(
            Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
            Optional.of(requestId.toString()),
            MessageBus.Data.with(
                UUID.randomUUID().toString(),
                "WaitingInfoForPublishedListing",
                waiting
            )
        );

        messageBus.publish(
            Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.of(requestId.toString()),
            MessageBus.Data.with(
                UUID.randomUUID().toString(),
                "GetListingPublicDetailsByIdResponse",
                response
            )
        );

        // Then
        waitForValue(receivedListingPublished, Duration.ofSeconds(10));

        final var received = receivedListingPublished.get();

        assertThat(received.getListingId()).isEqualTo(listingId);
        assertThat(received.getPublishedAt().toString()).isEqualTo(waiting.getPublishedAt().toString());
        assertThat(received.getData()).isEqualTo(response.getData());

        assertThat(isWaitingExists(requestId)).isFalse();
    }

    private boolean isWaitingExists(UUID requestId) {
        final var waiting = interactiveQueryService.retrieveQueryableStore(
                WAITINGS_STORE_NAME,
                QueryableStoreTypes.keyValueStore()
            )
            .get(requestId.toString());

        return waiting != null;
    }

    @TestConfiguration
    public static class Config {

        @Bean
        AtomicReference<ListingPublishedDTO> receivedListingPublished() {
            return new AtomicReference<>();
        }

        @Bean
        public KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
            return new KafkaStreamsInteractiveQueryService(streamsBuilderFactoryBean);
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
    }

    private UUID anyListingId() {
        return UUID.randomUUID();
    }

    ListingPublicDetailsDTO anyPublicListingDetails(UUID listingId) {
        return ListingPublicDetailsDTO.newBuilder()
            .setId(listingId)
            .setAddress(
                AddressDTO.newBuilder()
                    .setCity("city")
                    .setCountry("country")
                    .setStreet("street")
                    .setState("state")
                    .build()
            )
            .setCreatedAt(LocalDateTime.now().toString())
            .setDescription("description")
            .setStatus(ListingStatusDTO.PUBLISHED)
            .setHostId(UUID.randomUUID())
            .setRoomType(RoomTypeDTO.PRIVATE_ROOM)
            .setPropertyType(PropertyTypeDTO.APARTMENT)
            .setTitle("title")
            .setGuestCapacity(
                GuestsCapacityDTO.newBuilder()
                    .setAdults(1)
                    .setChildren(2)
                    .setInfants(3)
                    .build()
            )
            .setUpdatedAt(LocalDateTime.now().toString())
            .setPhotos(
                List.of(
                    ListingPhotoDTO.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setBucketName("bucketName")
                        .setObjectName("objectName")
                        .build()
                )
            )
            .build();
    }

    private WaitingInfoForPublishedListingDTO anyWaiting() {
        return WaitingInfoForPublishedListingDTO.newBuilder()
            .setListingId(anyListingId())
            .setRequestId(UUID.randomUUID())
            .setPublishedAt(LocalDateTime.now().toString())
            .build();
    }

    private GetListingPublicDetailsByIdResponseDTO anyResponse(UUID listingId) {
        return GetListingPublicDetailsByIdResponseDTO.newBuilder()
            .setParams(
                GetListingPublicDetailsByIdParamsDTO.newBuilder()
                    .setListingId(listingId)
                    .build()
            )
            .setData(anyPublicListingDetails(listingId))
            .build();
    }
}
