package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.generated.api.bus.Channels;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.listingsms.GetListingPublicDetailsByIdResponseDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.ListingPublishedDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.ReceivedListingDetailsForPublishedListingDTO;
import com.azat4dev.booking.searchlistingsms.generated.api.bus.dto.searchlistingsms.WaitingInfoForPublishedListingDTO;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Configuration
public class KafkaStreamsPublishedListingEnrichmentConfig {

    private final GetProducedWithForChannel producedWith;
    private final GetConsumedWithForChannel consumedWith;

    public static final String WAITINGS_STORE_NAME = "listing-info-waitings";

    @Bean("receivedListingDetailsStream")
    KStream<String, Message> receivedListingDetailsStream(StreamsBuilder builder) {
        return builder.stream(
            Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING.getValue(),
            consumedWith.run(Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING)
        );
    }

    @Bean("deleteProcessedWaitingsStream")
    KStream<String, Message> deleteProcessedWaitingsStream(
        @Qualifier("receivedListingDetailsStream")
        KStream<String, Message> receivedListingDetailsStream
    ) {
        return receivedListingDetailsStream.mapValues(value -> null);
    }

    @Bean("waitingsStream")
    KStream<String, Message> waitingsStream(
        StreamsBuilder builder,
        @Qualifier("deleteProcessedWaitingsStream")
        KStream<String, Message> deleteProcessedWaitingsStream
    ) {
        return builder.stream(
                Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
                consumedWith.run(Channels.INTERNAL_LISTING_EVENTS_STREAM)
            )
            .filter((key, message) -> message.payload() instanceof WaitingInfoForPublishedListingDTO)
            // Delete processed waitings
            .merge(deleteProcessedWaitingsStream);
    }

    @Bean("waitingsTable")
    KTable<String, Message> waitingsTable(
        @Qualifier("waitingsStream")
        KStream<String, Message> waitingsStream,
        GetSerdeForChannel getSerdeForChannel
    ) {
        final var store = Stores.persistentKeyValueStore(WAITINGS_STORE_NAME);

        return waitingsStream
            .toTable(
                Named.as(WAITINGS_STORE_NAME),
                Materialized.<String, Message>as(store)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(getSerdeForChannel.run(Channels.INTERNAL_LISTING_EVENTS_STREAM))
            );
    }

    @Bean("responsesStream")
    KStream<String, Message> responsesStream(StreamsBuilder builder) {
        return builder.stream(
            Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(),
            consumedWith.run(Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID)
        );
    }

    @Bean("receivedListingDetailsForPublishedListingStream")
    KStream<String, Message> receivedListingDetailsForPublishedListingStream(
        @Qualifier("responsesStream")
        KStream<String, Message> responsesStream,
        @Qualifier("waitingsTable")
        KTable<String, Message> waitingsTable
    ) {
        return responsesStream.join(waitingsTable, (responseMessage, waitingMessage) -> {

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
    }

    @Bean("inputJoinedMessagesStream")
    KStream<String, Message> inputJoinedMessagesStream(StreamsBuilder builder) {
        return builder.stream(
            Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING.getValue(),
            consumedWith.run(Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING)
        );
    }

    @Bean("enrichedListingPublishedStream")
    KStream<String, Message> enrichedListingPublishedStream(
        @Qualifier("inputJoinedMessagesStream")
        KStream<String, Message> inputJoinedMessagesStream
    ) {
        return inputJoinedMessagesStream
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
    }

    @Bean("listingPublishedStream")
    KStream<String, Message> listingPublishedStream(
        StreamsBuilder builder,
        @Qualifier("receivedListingDetailsForPublishedListingStream")
        KStream<String, Message> receivedListingDetailsForPublishedListingStream,
        @Qualifier("enrichedListingPublishedStream")
        KStream<String, Message> enrichedListingPublishedStream
    ) {
        receivedListingDetailsForPublishedListingStream.to(
            Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING.getValue(),
            producedWith.run(Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING)
        );

        enrichedListingPublishedStream.to(
            Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
            producedWith.run(Channels.INTERNAL_LISTING_EVENTS_STREAM)
        );

        return builder.stream(
            Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(),
            consumedWith.run(Channels.INTERNAL_LISTING_EVENTS_STREAM)
        );
    }
}