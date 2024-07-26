package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.infrastructure.bus.*;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(RandomEventIdGeneratorConfig.class)
@AllArgsConstructor
@Configuration
public class DefaultDomainEventsBusConfig {

    private final MessageBus<String> messageBus;
    private final MapAnyDomainEvent mapAnyDomainEvent;

    @Bean
    public DomainEventsBus domainEventsBus(
        EventIdGenerator eventIdGenerator,
        GetInputTopicForEvent getInputTopicForEvent,
        GetOutputTopicForEvent getOutputTopicForEvent,
        GetPartitionKeyForEvent<String> getPartitionKeyForEvent
    ) {

        return new DefaultDomainEventsBus<String>(
            messageBus,
            getInputTopicForEvent,
            getOutputTopicForEvent,
            getPartitionKeyForEvent,
            eventIdGenerator,
            mapAnyDomainEvent
        );
    }
}
