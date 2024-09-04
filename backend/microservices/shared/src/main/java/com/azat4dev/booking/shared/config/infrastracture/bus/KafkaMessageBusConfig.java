package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.*;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@EnableKafka
@EnableKafkaStreams
@Configuration
@AllArgsConstructor
public class KafkaMessageBusConfig {

    private final TimeProvider timeProvider;
    private final Serializer<LocalDateTime, String> mapLocalDateTime;

    @Bean
    public ProducerFactory<?, ?> kafkaProducerFactory(
        KafkaProperties kafkaProperties,
        ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers,
        ObjectProvider<SslBundles> sslBundles
    ) {
        Map<String, Object> properties = kafkaProperties.buildProducerProperties(sslBundles.getIfAvailable());

        properties.put(
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
            Optional.ofNullable(kafkaProperties.getProducer().getSecurity().getProtocol())
                .orElse("PLAINTEXT")
        );

        properties.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            ByteArraySerializer.class
        );

        DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(properties);
        String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
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
    CustomTopologyFactoriesForTopics customTopologyFactoriesForTopics(
        List<CustomTopologyForTopic> customTopologyForTopics
    ) {
        return new CustomTopologyFactoriesForTopics(customTopologyForTopics);
    }

    @Bean
    MessageBus messageBus(
        MessageSerializer messageSerializer,
        KafkaMessageBus.LocalDateTimeSerializer localDateTimeSerializer,
        KafkaAdmin kafkaAdmin,
        DefaultMakeTopologyForTopic makeTopologyForTopic,
        CustomTopologyFactoriesForTopics customTopologyFactoriesForTopics,
        KafkaStreamsConfiguration kafkaStreamsConfiguration,
        KafkaTemplate<String, byte[]> kafkaTemplate
    ) {

        kafkaAdmin.setAutoCreate(false);
        kafkaAdmin.initialize();

        return new KafkaMessageBus(
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
    Serde<Message> serde(
        MessageSerializer messageSerializer
    ) {
        return new BusMessageSerde(
            messageSerializer
        );
    }
}
