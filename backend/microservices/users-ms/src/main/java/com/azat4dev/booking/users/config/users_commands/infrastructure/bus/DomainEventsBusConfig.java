package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.shared.data.bus.*;
import com.azat4dev.booking.shared.domain.events.*;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.core.events.EventWithUserId;
import com.azat4dev.booking.users.config.common.properties.BusProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@EnableConfigurationProperties(BusProperties.class)
@AllArgsConstructor
@Configuration
public class DomainEventsBusConfig {

    private final TimeProvider timeProvider;
    private final BusProperties busProperties;
    private final Set<Class<? extends DomainEventPayload>> domainEventsClasses;

    @Bean
    EventIdGenerator eventIdGenerator() {
        return new RandomEventIdGenerator();
    }

    @Bean
    DomainEventsFactory domainEventsFactory(EventIdGenerator eventIdGenerator) {
        return new DomainEventsFactoryImpl(eventIdGenerator, timeProvider);
    }

    @Bean
    GetInputTopicForEvent getInputTopicForEvent(Set<Class<? extends DomainEventPayload>> domainEvents) {

        final var inputTopicsForCommands = domainEvents.stream()
            .filter(v -> Arrays.stream(v.getInterfaces()).anyMatch(i -> i.equals(Command.class)))
            .collect(Collectors.toMap(Function.identity(), v -> busProperties.getInternalCommandsTopicPrefix() + "." + v.getSimpleName()));

        final var userEventsTopic = busProperties.getPrefixForEventsTopics() + "." + busProperties.getUserEventsTopicName();

        return (eventClass) -> {

            final var commandTopic = inputTopicsForCommands.get(eventClass);
            if (commandTopic != null) {
                return commandTopic;
            }

            return userEventsTopic;
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
                if (event instanceof EventWithUserId inst) {
                    return Optional.of(inst.userId().toString());
                }

                return Optional.empty();
            }
        };
    }

    @Bean
    public DomainEventsBus domainEventsBus(
        MessageBus<String> messageBus,
        EventIdGenerator eventIdGenerator,
        GetInputTopicForEvent getInputTopicForEvent,
        GetOutputTopicForEvent getOutputTopicForEvent,
        GetPartitionKeyForEvent<String> getPartitionKeyForEvent
    ) {

        final var classesByNames = domainEventsClasses.stream().collect(Collectors.toMap(Class::getSimpleName, v -> v));

        return new KafkaDomainEventsBus<String>(
            messageBus,
            getInputTopicForEvent,
            getOutputTopicForEvent,
            getPartitionKeyForEvent,
            classesByNames::get,
            eventIdGenerator
        );
    }

    @KafkaListener(topics = "com.azat4dev.users.events.user_events_stream", groupId = "testgroup", concurrency = "1")
    public void listen(String message) {
        System.out.println("Received Messasge in group foo: " + message);
    }
}
