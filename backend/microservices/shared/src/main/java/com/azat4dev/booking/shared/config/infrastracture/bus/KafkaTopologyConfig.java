package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.TopicListener;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.*;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Topology;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class KafkaTopologyConfig {

    private final List<TopicListener> topicListeners;
    private final List<TopicStreamConfigurator> customStreamConfigurators;

    @Bean
    GetDefaultStreamFactory getDefaultStreamFactory(GetSerdeForTopic getSerdeForTopic) {
        return new GetDefaultStreamFactory(getSerdeForTopic);
    }

    @Bean
    KafkaTopologyBuilder topologyBuilder(
        GetDefaultStreamFactory getDefaultStreamFactory
    ) {
        return new KafkaTopologyBuilder(
            getDefaultStreamFactory
        );
    }

    @Bean
    Topology topology(
        List<StreamFactoryForTopic> factories,
        KafkaTopologyBuilder topologyBuilder
    ) {

        final var topicConfigurators = new LinkedList<TopicStreamConfigurator>();
        topicListeners
            .forEach(i -> {
                topicConfigurators.add(
                    new TopicStreamConfigurator(
                        i.topic(),
                        new KafkaStreamConfiguratorForMessageListener(i.messageListener())
                    )
                );
            });

        topicConfigurators.addAll(customStreamConfigurators);
        return topologyBuilder.build(
            factories,
            topicConfigurators
        );
    }
}
