package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.shared.data.bus.KafkaDomainEventsBus;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.function.Function;

@Configuration
public class BusConfig {

    @Bean
    public DomainEventsBus domainEventsBus(
        KafkaTemplate<String, String> kafkaTemplate,
        DomainEventSerializer domainEventSerializer,
        Function<String, ConcurrentMessageListenerContainer<String, String>> containerFactory,
        TimeProvider timeProvider,
        EventIdGenerator eventIdGenerator
    ) {
        return new KafkaDomainEventsBus(
            kafkaTemplate,
            domainEventSerializer,
            containerFactory,
            timeProvider,
            eventIdGenerator
        );
    }
}
