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
    private final GetDefaultStreamFactoryForTopic getDefaultStreamFactory;

    public void build(
        List<StreamFactoryForTopic> streamFactories,
        List<StreamConfiguratorForTopic> streamConfigurators
    ) {

        final var duplicatedTopic = streamFactories.stream()
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

        final var factoriesByTopics = streamFactories.stream()
            .collect(
                Collectors.toMap(
                    StreamFactoryForTopic::topic,
                    i -> i
                )
            );

        final var streamConfiguratorsByTopics = streamConfigurators.stream()
            .collect(
                Collectors.groupingBy(
                    StreamConfiguratorForTopic::channel,
                    Collectors.toList()
                )
            );

        for (final var streamConfiguratorsForTopic : streamConfiguratorsByTopics.entrySet()) {

            final var topic = streamConfiguratorsForTopic.getKey();
            final var configurators = streamConfiguratorsForTopic.getValue();

            final var customFactory = factoriesByTopics.get(topic);

            var factory = customFactory != null ? customFactory.factory() : getDefaultStreamFactory.run(topic);
            final var stream = factory.make(streamsBuilder);

            if (configurators == null || configurators.isEmpty()) {
                continue;
            }

            for (final var configuratorForTopic : configurators) {
                configuratorForTopic.configurator().configure(stream);
            }
        }
    }
}
