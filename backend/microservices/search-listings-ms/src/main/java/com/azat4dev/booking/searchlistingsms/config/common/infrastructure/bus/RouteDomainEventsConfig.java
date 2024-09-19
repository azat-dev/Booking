package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.config.common.properties.BusProperties;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultDomainEventsBusConfig;
import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputChannelForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.GetOutputChannelForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.GetPartitionKeyForEvent;
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
    GetInputChannelForEvent getInputChannelForEvent(Set<Class<? extends DomainEventPayload>> mappedDomainEvents) {

        final var inputChannelsForCommands = mappedDomainEvents.stream()
            .filter(v -> Arrays.stream(v.getInterfaces()).anyMatch(i -> i.equals(Command.class)))
            .collect(Collectors.toMap(Function.identity(), v -> busProperties.getInternalCommandsChannelPrefix() + "." + v.getSimpleName()));

        final var listingEventsChannel = busProperties.getPrefixForEventsChannels() + "." + busProperties.getListingEventsChannelName();

        return (eventClass) -> {

            final var commandChannel = inputChannelsForCommands.get(eventClass);
            if (commandChannel != null) {
                return commandChannel;
            }

            return listingEventsChannel;
        };
    }

    @Bean
    GetOutputChannelForEvent getOutputChannelForEvent(GetInputChannelForEvent getInputChannel) {
        return (event) -> getInputChannel.execute(event.getClass());
    }

    @Bean
    GetPartitionKeyForEvent getPartitionKeyForEvent() {
        return new GetPartitionKeyForEvent() {
            @Override
            public <T extends DomainEventPayload> Optional<String> execute(T event) {
//                if (event instanceof EventWithListingId inst) {
//                    return Optional.of(inst.listingId().toString());
//                }

                return Optional.empty();
            }
        };
    }
}
