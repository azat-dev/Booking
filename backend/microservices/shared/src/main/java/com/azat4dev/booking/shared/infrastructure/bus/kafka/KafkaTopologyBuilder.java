package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class KafkaTopologyBuilder {

    private final StreamsBuilder streamsBuilder;
    private final GetDefaultStreamFactory getDefaultStreamFactory;

    public void build(
        List<StreamFactoryForTopic> factories,
        List<TopicStreamConfigurator> listeners
    ) {

        final var duplicatedTopic = factories.stream()
            .collect(
                Collectors.groupingBy(
                    StreamFactoryForTopic::topic,
                    Collectors.counting()
                )
            )
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 1)
            .findFirst();

        if (duplicatedTopic.isPresent()) {
            log.error("Duplicated topic: {}", duplicatedTopic.get().getKey());
            throw new RuntimeException("Duplicated stream factory for topic: " + duplicatedTopic.get().getKey());
        }

        final var factoriesByTopics = factories.stream()
            .collect(
                Collectors.toMap(
                    StreamFactoryForTopic::topic,
                    i -> i
                )
            );

        final var listenersByTopics = listeners.stream()
            .collect(
                Collectors.groupingBy(
                    TopicStreamConfigurator::topic,
                    Collectors.toList()
                )
            );

        for (final var listenerItem : listenersByTopics.entrySet()) {

            final var topic = listenerItem.getKey();
            final var listenersForTopic = listenerItem.getValue();

            final var customFactory = factoriesByTopics.get(topic);

            var factory = customFactory != null ? customFactory.factory() : getDefaultStreamFactory.run(topic);
            final var stream = factory.make(streamsBuilder);

            if (listenersForTopic == null || listenersForTopic.isEmpty()) {
                continue;
            }

            for (final var listener : listenersForTopic) {
                listener.configurator().configure(stream);
            }
        }
    }
}
