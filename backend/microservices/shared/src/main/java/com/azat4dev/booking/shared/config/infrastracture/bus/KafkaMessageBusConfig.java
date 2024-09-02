package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.*;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@EnableKafka
@EnableKafkaStreams
@Configuration
@AllArgsConstructor
public class KafkaMessageBusConfig {

    private final TimeProvider timeProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Serializer<LocalDateTime, String> mapLocalDateTime;

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
    CustomTopologyFactoriesForTopics customTopologyFactoriesForTopics(
        List<CustomTopologyForTopic> customTopologyForTopics
    ) {
        return new CustomTopologyFactoriesForTopics(customTopologyForTopics);
    }

    @Bean
    MessageBus<String> messageBus(
        MessageSerializer<String> messageSerializer,
        KafkaMessageBus.LocalDateTimeSerializer localDateTimeSerializer,
        KafkaAdmin kafkaAdmin,
        DefaultMakeTopologyForTopic makeTopologyForTopic,
        CustomTopologyFactoriesForTopics customTopologyFactoriesForTopics,
        KafkaStreamsConfiguration kafkaStreamsConfiguration
    ) {

        kafkaAdmin.setAutoCreate(false);
        kafkaAdmin.initialize();

        return new KafkaMessageBus<>(
            messageSerializer,
            kafkaTemplate,
            timeProvider,
            localDateTimeSerializer,
            customTopologyFactoriesForTopics,
            makeTopologyForTopic,
            kafkaStreamsConfiguration.asProperties()
        );
    }

    @Bean
    Serde<MessageBus.ReceivedMessage> serde(
        MessageSerializer<String> messageSerializer,
        KafkaMessageBus.LocalDateTimeSerializer localDateTimeSerializer
    ) {
        return new BusMessageSerde(messageSerializer, localDateTimeSerializer);
    }
}
