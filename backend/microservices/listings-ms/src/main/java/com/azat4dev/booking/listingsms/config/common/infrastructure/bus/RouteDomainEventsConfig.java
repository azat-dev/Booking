package com.azat4dev.booking.listingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.listingsms.commands.domain.events.EventWithListingId;
import com.azat4dev.booking.listingsms.config.common.properties.BusProperties;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultDomainEventsBusConfig;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputTopicForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.GetOutputTopicForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.GetPartitionKeyForEvent;
import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@Import(DefaultDomainEventsBusConfig.class)
@EnableConfigurationProperties(BusProperties.class)
@AllArgsConstructor
public class RouteDomainEventsConfig {

    private final BusProperties busProperties;

    @Bean
    GetInputTopicForEvent getInputTopicForEvent(Set<Class<? extends DomainEventPayload>> mappedDomainEvents) {

        final var inputTopicsForCommands = mappedDomainEvents.stream()
            .filter(v -> Arrays.stream(v.getInterfaces()).anyMatch(i -> i.equals(Command.class)))
            .collect(Collectors.toMap(Function.identity(), v -> busProperties.getInternalCommandsTopicPrefix() + "." + v.getSimpleName()));

        final var listingEventsTopic = busProperties.getPrefixForEventsTopics() + "." + busProperties.getListingEventsTopicName();

        return (eventClass) -> {

            final var commandTopic = inputTopicsForCommands.get(eventClass);
            if (commandTopic != null) {
                return commandTopic;
            }

            return listingEventsTopic;
        };
    }

    @Bean
    GetOutputTopicForEvent getOutputTopicForEvent(GetInputTopicForEvent getInputTopic) {
        return (event) -> getInputTopic.execute(event.getClass());
    }

    @Bean
    GetPartitionKeyForEvent<String> getPartitionKeyForEvent() {
        return new GetPartitionKeyForEvent<String>() {
            @Override
            public <T extends DomainEventPayload> Optional<String> execute(T event) {
                if (event instanceof EventWithListingId inst) {
                    return Optional.of(inst.listingId().toString());
                }

                return Optional.empty();
            }
        };
    }
}
