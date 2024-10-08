package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenerForChannel;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenersForChannel;
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
        List<NewKafkaStreamForTopic> newKafkaStreamForTopics,
        KafkaTopologyBuilder topologyBuilder,
        List<NewMessageListenerForChannel> channelListeners,
        List<NewMessageListenersForChannel> channelListenersList,
        List<StreamConnectorForTopic> customStreamConfigurators
    ) {

        final var defaultTopicConfigurators = Stream.concat(
            channelListeners.stream(),
            channelListenersList.stream().flatMap(i -> i.items().stream())
        ).map(i -> new StreamConnectorForTopic(
            i.channel(),
            new KafkaStreamConnectToMessageListener(i.messageListener())
        ));

        final var allTopicStreamConfigurators = Stream.concat(
            defaultTopicConfigurators,
            customStreamConfigurators.stream()
        ).toList();

        topologyBuilder.build(
            newKafkaStreamForTopics,
            allTopicStreamConfigurators
        );

        return new TopologyConfigured();
    }
}
