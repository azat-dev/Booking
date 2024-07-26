package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.GetNumberOfConsumersForTopic;
import com.azat4dev.booking.shared.infrastructure.bus.KafkaMessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

@Slf4j
@Import(JsonMessageBusSerializerConfig.class)
@Configuration
public class KafkaMessageBusConfig {

    private final TimeProvider timeProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ConcurrentKafkaListenerContainerFactory<String, String> containerFactory;
    private final Serializer<LocalDateTime, String> mapLocalDateTime;

    public KafkaMessageBusConfig(
        TimeProvider timeProvider,
        KafkaTemplate<String, String> kafkaTemplate,
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory,
        Serializer<LocalDateTime, String> mapLocalDateTime
    ) {
        this.timeProvider = timeProvider;
        this.kafkaTemplate = kafkaTemplate;
        this.containerFactory = containerFactory;
        this.mapLocalDateTime = mapLocalDateTime;
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
