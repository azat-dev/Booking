package com.azat4dev.booking.searchlistingsms.acl.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus.InternalTopics;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.Channels;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.*;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.ListingPublishedDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.ReceivedListingDetailsForPublishedListingDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.WaitingInfoForPublishedListingDTO;
import com.azat4dev.booking.searchlistingsms.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListener;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenerForChannel;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.GetSerdeForTopic;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.NewKafkaStreamForTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.Stores;
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

    @Test
    void test_givenWaitingForListingDetails_whenReceivedResponse_thenPublishEnrichedListingPublishedMessageAndDeleteRecordFromWaitingsTable() throws InterruptedException {

        // Given
        final var listingId = anyListingId();
        final var publishedAt = LocalDateTime.now();

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
                    .setPublishedAt(publishedAt.toString())
                    .build()
            )
        );

        messageBus.publish(
            Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            Optional.of(requestId.toString()),
            MessageBus.Data.with(
                UUID.randomUUID().toString(),
                "GetListingPublicDetailsByIdResponse",
                GetListingPublicDetailsByIdResponseDTO.newBuilder()
                    .setParams(
                        GetListingPublicDetailsByIdParamsDTO.newBuilder()
                            .setListingId(listingId)
                            .build()
                    )
                    .setData(anyPublicListingDetails(listingId))
                    .build()
            )
        );

        // Then
        waitForValue(receivedListingPublished, Duration.ofSeconds(10));

        final var savedWating = interactiveQueryService.retrieveQueryableStore(
                "listinginfowaitings",
                QueryableStoreTypes.keyValueStore()
            )
            .get(requestId.toString());

        assertThat(savedWating).isNull();
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

        static Consumed<String, Message> consumedWith(GetSerdeForChannel getSerdeForChannel, Channels channel) {
            return Consumed.with(
                Serdes.String(),
                getSerdeForChannel.run(channel)
            );
        }

        interface GetSerdeForChannel {
            Serde<Message> run(Channels channel);
        }

        static Produced<String, Message> producedWith(GetSerdeForChannel getSerdeForChannel, Channels channel) {
            return Produced.with(
                Serdes.String(),
                getSerdeForChannel.run(channel)
            );
        }

        @Bean
        GetSerdeForChannel getSerdeForChannel(GetSerdeForTopic getSerdeForTopic) {
            return channel -> getSerdeForTopic.run(channel.getValue());
        }

        @Bean
        NewKafkaStreamForTopic streamFactoryForTopic(GetSerdeForChannel getSerdeForChannel) {
            return new NewKafkaStreamForTopic(
                InternalTopics.LISTING_PUBLISHED.getValue(),
                builder -> {

                    final var storeName = "listinginfowaitings";

                    final var store = Stores.persistentKeyValueStore(storeName);

                    final var receivedListingDetailsStream = builder.stream(
                        Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING.getValue(),
                        consumedWith(getSerdeForChannel, Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING)
                    );

                    final var deleteProcessedWaitingsStream = receivedListingDetailsStream.<Message>mapValues(value -> null);

                    // Keep waitings in table
                    final var waitingsStream = builder.stream(
                            Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
                            consumedWith(getSerdeForChannel, Channels.INTERNAL_LISTING_EVENTS_STREAM)
                        )
                        .filter((key, message) -> message.payload() instanceof WaitingInfoForPublishedListingDTO)
                        // Delete processed waitings
                        .merge(deleteProcessedWaitingsStream);

                    waitingsStream.print(Printed.<String, Message>toSysOut().withLabel("waitingsStream"));

                    final var waitingsTable = waitingsStream
                        .toTable(
                            Named.as(storeName),
                            Materialized.<String, Message>as(store)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(getSerdeForChannel.run(Channels.INTERNAL_LISTING_EVENTS_STREAM))
                        );

                    final var responsesStream = builder.stream(
                        Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
                        consumedWith(getSerdeForChannel, Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID)
                    );

                    // Merge responses with waitings and publish enriched messages
                    final var receivedListingDetailsForPublishedListingStream = responsesStream.join(waitingsTable, (responseMessage, waitingMessage) -> {

                        final var response = responseMessage.payloadAs(GetListingPublicDetailsByIdResponseDTO.class);
                        final var waiting = waitingMessage.payloadAs(WaitingInfoForPublishedListingDTO.class);

                        return new Message(
                            UUID.randomUUID().toString(),
                            "ReceivedListingDetailsForPublishedListing",
                            LocalDateTime.now(),
                            Optional.of(waiting.getListingId().toString()),
                            Optional.empty(),
                            ReceivedListingDetailsForPublishedListingDTO.newBuilder()
                                .setListingId(waiting.getListingId())
                                .setRequestId(waiting.getRequestId().toString())
                                .setListingDetails(response.getData())
                                .setWaitingInfo(waiting)
                                .build()
                        );
                    });

                    receivedListingDetailsForPublishedListingStream.to(
                        Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING.getValue(),
                        producedWith(getSerdeForChannel, Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING)
                    );

                    final var inputJoinedMessagesStream = builder.stream(
                        Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING.getValue(),
                        consumedWith(getSerdeForChannel, Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING)
                    );

                    final var enrichedListingPublishedStream = inputJoinedMessagesStream
                        .map((key, value) -> {
                            final var payload = value.payloadAs(ReceivedListingDetailsForPublishedListingDTO.class);
                            final var listingId = payload.getListingId();

                            final var outputValue = new Message(
                                UUID.randomUUID().toString(),
                                "ListingPublished",
                                LocalDateTime.now(),
                                Optional.empty(),
                                Optional.empty(),
                                ListingPublishedDTO.newBuilder()
                                    .setListingId(listingId)
                                    .setPublishedAt(payload.getWaitingInfo().getPublishedAt())
                                    .setData(payload.getListingDetails())
                                    .build()
                            );

                            return KeyValue.pair(
                                listingId.toString(),
                                outputValue
                            );
                        });

                    enrichedListingPublishedStream.to(
                        Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
                        producedWith(getSerdeForChannel, Channels.INTERNAL_LISTING_EVENTS_STREAM)
                    );

                    return builder.stream(
                        Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
                        consumedWith(getSerdeForChannel, Channels.INTERNAL_LISTING_EVENTS_STREAM)
                    );
                }
            );
        }
    }
}
