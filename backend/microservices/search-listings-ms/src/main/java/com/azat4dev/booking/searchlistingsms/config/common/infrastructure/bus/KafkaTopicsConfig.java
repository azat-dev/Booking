package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.generated.api.bus.Channels;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Slf4j
@AllArgsConstructor
@Configuration
public class KafkaTopicsConfig {

    @Bean
    KafkaAdmin.NewTopics newTopicList() {
        return new KafkaAdmin.NewTopics(
            new NewTopic(Channels.INTERNAL_LISTING_EVENTS_STREAM.getValue(), 20, (short) 1),
            new NewTopic(Channels.RECEIVE_GET_LISTING_PUBLIC_DETAILS_BY_ID.getValue(), 20, (short) 1),
            new NewTopic(Channels.INTERNAL_RECEIVED_LISTING_DETAILS_FOR_PUBLISHED_LISTING.getValue(), 20, (short) 1)
        );
    }
}