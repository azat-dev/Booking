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
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
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
    InitializedTopics initializedTopics(
        KafkaAdmin kafkaAdmin
    ) {
        kafkaAdmin.setAutoCreate(false);
        kafkaAdmin.initialize();

        return new InitializedTopics();
    }

    @Bean
    MessageBus messageBus(
        InitializedTopics initializedTopics,
        MessageSerializersForTopics messageSerializers,
        KafkaTemplate<String, byte[]> kafkaTemplate,
        TimeProvider timeProvider
    ) {
        return new KafkaMessageBus(
            messageSerializers,
            kafkaTemplate,
            timeProvider
        );
    }

    @Bean
    MessageDeserializersForTopics messageDeserializersForTopics(List<CustomMessageDeserializerForTopics> items) {
        return new MessageDeserializersForTopics(items);
    }

    @Bean
    MessageSerializersForTopics messageSerializersForTopics(List<CustomMessageSerializerForTopics> items) {
        return new MessageSerializersForTopics(items);
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration streamsConfiguration(
        @Value("${spring.kafka.bootstrap-servers}")
        String bootstrapServers
    ) {
        Map<String, Object> props = new HashMap<>();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilderFactoryBeanConfigurer configurer() {
        return fb -> fb.setStateListener((newState, oldState) -> {
            System.out.println("State transition from " + oldState + " to " + newState);
        });
    }

    public static class InitializedTopics {}
}
