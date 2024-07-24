package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.shared.data.bus.*;
import com.azat4dev.booking.shared.data.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.data.serializers.Serializer;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Configuration
public class MessageBusConfig {

    private final ObjectMapper objectMapper;
    private final TimeProvider timeProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ConcurrentKafkaListenerContainerFactory<String, String> containerFactory;
    private final Serializer<LocalDateTime, String> mapLocalDateTime;
    private final List<MapDomainEvent<?, ?>> mappers;

    @Bean
    Map<String, MapDomainEvent<DomainEventPayload, ?>> dtoMapperForEventClass() {
        return mappers.stream()
            .collect(
                Collectors.toMap(
                    v -> v.getOriginalClass().getSimpleName(),
                    v -> (MapDomainEvent<DomainEventPayload, ?>) v
                )
            );
    }

    @Bean
    MessageSerializerJSON.GetClassForMessageType getClassForMessageType(Map<String, MapDomainEvent<DomainEventPayload, ?>> dtoMapperForEventClass) {

        return eventType -> {
            final var dtoMapper = dtoMapperForEventClass.get(eventType);
            if (dtoMapper == null) {
                log.atError()
                    .addArgument(eventType)
                    .log("No mapper found for event type: {}");
                return null;
            }
            return dtoMapper.getSerializedClass();
        };
    }

    @Bean
    MessageSerializerJSON messageSerializerJSON(MessageSerializerJSON.GetClassForMessageType getDtoClassForMessageType) {
        return new MessageSerializerJSON(
            objectMapper,
            getDtoClassForMessageType
        );
    }

    @Bean
    MessageSerializer<String> messageSerializer(
        MessageSerializerJSON jsonMessageSerializer,
        MapAnyDomainEvent mapAnyDomainEvent
    ) {

        return new MessageSerializerJSONWithDomainEvents(
            jsonMessageSerializer,
            mapAnyDomainEvent
        );
    }

    @Bean
    KafkaMessageBus.LocalDateTimeSerializer localDateTimeSerializer() {
        return new KafkaMessageBus.LocalDateTimeSerializer() {
            @Override
            public String serialize(LocalDateTime time) {
                return mapLocalDateTime.serialize(time);
            }

            @Override
            public LocalDateTime deserialize(String time) {
                return mapLocalDateTime.deserialize(time);
            }
        };
    }

    @Bean
    GetNumberOfConsumersForTopic getNumberOfConsumersForTopic() {
        return topic -> 3;
    }

    @Bean
    MessageBus<String> messageBus(
        MessageSerializer<String> messageSerializer,
        KafkaMessageBus.LocalDateTimeSerializer localDateTimeSerializer,
        KafkaAdmin kafkaAdmin,
        KafkaAdmin.NewTopics newTopics,
        GetNumberOfConsumersForTopic getNumberOfConsumersForTopic
    ) {

        kafkaAdmin.setAutoCreate(false);
        kafkaAdmin.initialize();

        return new KafkaMessageBus<>(
            getNumberOfConsumersForTopic,
            messageSerializer,
            kafkaTemplate,
            containerFactory,
            timeProvider,
            localDateTimeSerializer
        );
    }
}
