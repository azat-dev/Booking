package com.azat4dev.booking.listingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.listingsms.config.common.properties.BusProperties;
import com.azat4dev.booking.shared.data.bus.KafkaDomainEventsBus;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.data.serializers.Mapper;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(BusProperties.class)
@AllArgsConstructor
@Configuration
public class BusConfig {

    private final TimeProvider timeProvider;
    private final BusProperties busProperties;

    @Bean
    public DomainEventsBus domainEventsBus(
        KafkaTemplate<String, String> kafkaTemplate,
        DomainEventSerializer domainEventSerializer,
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory,
        EventIdGenerator eventIdGenerator,
        List<Class<DomainEventPayload>> classes,
        Mapper<LocalDateTime, String> mapLocalDateTime
    ) {

        final var classesByNames = classes.stream().collect(Collectors.toMap(Class::getSimpleName, v -> v));

        final KafkaDomainEventsBus.GetClassForEventType getClass = eventType -> {
            return classesByNames.get(eventType);
        };

        final KafkaDomainEventsBus.GetTopic getTopic = event -> {
            return busProperties.getEventsTopicPrefix() + "." + event.getSimpleName();
        };

        return new KafkaDomainEventsBus(
            getClass,
            getTopic,
            kafkaTemplate,
            domainEventSerializer,
            containerFactory,
            timeProvider,
            eventIdGenerator,
            new KafkaDomainEventsBus.LocalDateTimeSerializer() {
                @Override
                public String serialize(LocalDateTime time) {
                    return mapLocalDateTime.toDTO(time);
                }

                @Override
                public LocalDateTime deserialize(String time) {
                    return mapLocalDateTime.toDomain(time);
                }
            }
        );
    }
}
