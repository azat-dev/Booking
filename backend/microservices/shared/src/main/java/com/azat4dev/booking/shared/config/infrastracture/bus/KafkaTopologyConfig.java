package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.NewTopicListeners;
import com.azat4dev.booking.shared.infrastructure.bus.NewTopicMessageListener;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.*;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@AllArgsConstructor
public class KafkaTopologyConfig {

    @Bean
    GetDefaultStreamFactoryForTopic getDefaultStreamFactory(GetSerdeForTopic getSerdeForTopic) {
        return new GetDefaultStreamFactoryForTopic(getSerdeForTopic);
    }

    @Bean
    KafkaTopologyBuilder topologyBuilder(
        StreamsBuilder streamsBuilder,
        GetDefaultStreamFactoryForTopic getDefaultStreamFactory
    ) {
        return new KafkaTopologyBuilder(
            streamsBuilder,
            getDefaultStreamFactory
        );
    }

    @Bean
    TopologyConfigured topology(
        List<StreamFactoryForTopic> factories,
        KafkaTopologyBuilder topologyBuilder,
        List<NewTopicMessageListener> topicListeners,
        List<NewTopicListeners> topicListenersList,
        List<TopicStreamConfigurator> customStreamConfigurators
    ) {

        final var defaultTopicConfigurators = Stream.concat(
            topicListeners.stream(),
            topicListenersList.stream().flatMap(i -> i.items().stream())
        ).map(i -> new TopicStreamConfigurator(
            i.topic(),
            new KafkaStreamConfiguratorForMessageListener(i.messageListener())
        ));

        final var allTopicStreamConfigurators = Stream.concat(
            defaultTopicConfigurators,
            customStreamConfigurators.stream()
        ).toList();

        topologyBuilder.build(
            factories,
            allTopicStreamConfigurators
        );

        return new TopologyConfigured();
    }
}
