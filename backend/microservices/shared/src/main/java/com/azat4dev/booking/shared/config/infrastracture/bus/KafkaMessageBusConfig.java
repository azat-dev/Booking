package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.KafkaMessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.CustomMessageDeserializerForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.CustomMessageSerializerForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializersForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializersForTopics;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Import({
    KafkaTopologyConfig.class
})
@EnableKafka
@EnableKafkaStreams
@Configuration
@AllArgsConstructor
public class KafkaMessageBusConfig {

    private final TimeProvider timeProvider;

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
    MessageBus messageBus(
        MessageSerializersForTopics messageSerializers,
        KafkaAdmin kafkaAdmin,
        KafkaTemplate<String, byte[]> kafkaTemplate
    ) {

        kafkaAdmin.setAutoCreate(false);
        kafkaAdmin.initialize();

        return new KafkaMessageBus(
            messageSerializers,
            kafkaTemplate,
            timeProvider
        );
    }

    @Bean
    KafkaStreams kafkaStreams(
        KafkaStreamsConfiguration kafkaStreamsConfiguration,
        Topology topology,
        MessageBus messageBus // execute only after messageBus bean is created
    ) {

        final var streams = new KafkaStreams(
            topology,
            kafkaStreamsConfiguration.asProperties()
        );

        return streams;
    }

    @Bean
    MessageDeserializersForTopics messageDeserializersForTopics(List<CustomMessageDeserializerForTopics> items) {
        return new MessageDeserializersForTopics(items);
    }

    @Bean
    MessageSerializersForTopics messageSerializersForTopics(List<CustomMessageSerializerForTopics> items) {
        return new MessageSerializersForTopics(items);
    }
}
