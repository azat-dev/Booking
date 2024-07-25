package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.data.bus.*;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;
import java.util.stream.Collectors;

@Import(RandomEventIdGeneratorConfig.class)
@AllArgsConstructor
@Configuration
public class DefaultDomainEventsBusConfig {

    private final Set<Class<? extends DomainEventPayload>> domainEventsClasses;
    private final MessageBus<String> messageBus;

    @Bean
    public DomainEventsBus domainEventsBus(
        EventIdGenerator eventIdGenerator,
        GetInputTopicForEvent getInputTopicForEvent,
        GetOutputTopicForEvent getOutputTopicForEvent,
        GetPartitionKeyForEvent<String> getPartitionKeyForEvent
    ) {

        final var classesByNames = domainEventsClasses.stream()
            .collect(Collectors.toMap(Class::getSimpleName, v -> v));

        return new DefaultDomainEventsBus<String>(
            messageBus,
            getInputTopicForEvent,
            getOutputTopicForEvent,
            getPartitionKeyForEvent,
            classesByNames::get,
            eventIdGenerator
        );
    }
}
