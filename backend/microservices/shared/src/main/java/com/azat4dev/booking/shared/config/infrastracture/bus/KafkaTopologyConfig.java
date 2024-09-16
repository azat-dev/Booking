package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.NewTopicListener;
import com.azat4dev.booking.shared.infrastructure.bus.NewTopicListeners;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.*;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@AllArgsConstructor
public class KafkaTopologyConfig {

    @Bean
    GetDefaultStreamFactory getDefaultStreamFactory(GetSerdeForTopic getSerdeForTopic) {
        return new GetDefaultStreamFactory(getSerdeForTopic);
    }

    @Bean
    KafkaTopologyBuilder topologyBuilder(
        StreamsBuilder streamsBuilder,
        GetDefaultStreamFactory getDefaultStreamFactory
    ) {
        return new KafkaTopologyBuilder(
            streamsBuilder,
            getDefaultStreamFactory
        );
    }

    @Bean
    BeanFactoryPostProcessor topology(
        List<StreamFactoryForTopic> factories,
        KafkaTopologyBuilder topologyBuilder,
        List<NewTopicListener> topicListeners,
        List<NewTopicListeners> topicListenersList,
        List<TopicStreamConfigurator> customStreamConfigurators
    ) {

        return beanFactory -> {

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
        };
    }
}
