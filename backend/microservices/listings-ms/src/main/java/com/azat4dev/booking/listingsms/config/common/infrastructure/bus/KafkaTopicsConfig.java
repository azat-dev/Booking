package com.azat4dev.booking.listingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.listingsms.config.common.properties.BusProperties;
import com.azat4dev.booking.listingsms.generated.api.bus.Channels;
import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Configuration
public class KafkaTopicsConfig {

    @Bean
    KafkaAdmin.NewTopics newTopicList(
        BusProperties busProperties,
        Set<Class<? extends DomainEventPayload>> domainEvents
    ) {

        final var topics = new LinkedList<NewTopic>();
        topics.add(
            new NewTopic(
                busProperties.getPrefixForEventsTopics() + "." + busProperties.getListingEventsTopicName(),
                Optional.ofNullable(busProperties.getListingEventsTopicNumberOfPartitions()),
                Optional.ofNullable(busProperties.getListingEventsTopicReplicationFactor())
            )
        );

        topics.add(
            new NewTopic(
                Channels.QUERIES_RESPONSES__GET_PUBLIC_LISTING_DETAILS_BY_ID.getValue(),
                Optional.of(1),
                Optional.empty()
            )
        );

        domainEvents.forEach(event -> {
            final var isCommand = Arrays.stream(event.getInterfaces())
                .anyMatch(i -> i.equals(Command.class));

            if (!isCommand) {
                return;
            }

            topics.add(
                new NewTopic(
                    busProperties.getInternalCommandsTopicPrefix() + "." + event.getSimpleName(),
                    Optional.ofNullable(busProperties.getInternalCommandsTopicNumberOfPartitions()),
                    Optional.ofNullable(busProperties.getInternalCommandsTopicReplicationFactor())
                )
            );
        });

        log.atInfo()
            .addArgument(topics)
            .log("Created topics: {}");

        return new KafkaAdmin.NewTopics(topics.toArray(new NewTopic[0]));
    }
}