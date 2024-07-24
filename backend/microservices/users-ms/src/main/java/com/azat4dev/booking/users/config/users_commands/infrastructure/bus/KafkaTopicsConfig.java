package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.users.config.common.properties.BusProperties;
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
                busProperties.getPrefixForEventsTopics() + "." + busProperties.getUserEventsTopicName(),
                Optional.ofNullable(busProperties.getUserEventsTopicPartitions()),
                Optional.ofNullable(busProperties.getUserEventsTopicReplicationFactor())
            )
        );

        domainEvents.forEach(event -> {
            final var isCommand = Arrays.stream(event.getInterfaces()).anyMatch(i -> i.equals(Command.class));
            if (!isCommand) {
                return;
            }

            topics.add(
                new NewTopic(
                    busProperties.getInternalCommandsTopicPrefix() + "." + event.getSimpleName(),
                    Optional.ofNullable(busProperties.getInternalCommandsTopicPartitions()),
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
