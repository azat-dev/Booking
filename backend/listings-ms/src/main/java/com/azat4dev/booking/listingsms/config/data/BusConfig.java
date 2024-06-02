package com.azat4dev.booking.listingsms.config.data;

import com.azat4dev.booking.listingsms.commands.data.serializer.DomainEventsSerializerImpl;
import com.azat4dev.booking.shared.data.bus.KafkaDomainEventsBus;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.shared.domain.event.EventIdGenerator;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Bean
    public DomainEventSerializer domainEventSerializer(ObjectMapper objectMapper) {
        return new DomainEventsSerializerImpl(objectMapper);
    }
}
