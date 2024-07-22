package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.shared.data.bus.KafkaDomainEventsBus;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.data.serializers.Mapper;
import com.azat4dev.booking.shared.domain.events.*;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.config.common.properties.BusProperties;
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
    EventIdGenerator eventIdGenerator() {
        return new RandomEventIdGenerator();
    }

    @Bean
    DomainEventsFactory domainEventsFactory(EventIdGenerator eventIdGenerator) {
        return new DomainEventsFactoryImpl(eventIdGenerator, timeProvider);
    }

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

        final KafkaDomainEventsBus.GetTopic getTopic = messageClass -> {
            if (messageClass.equals(SendVerificationEmail.class)) {
                return busProperties.getPrefixForInternalCommandsTopic() + "send_verification_email";
            }

            return busProperties.getUserEventsTopic() + ".events.user_events_stream";
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
