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
        List<NewKafkaStreamForTopic> streamsForTopics,
        List<StreamConnectorForTopic> streamConfigurators
    ) {

        final var duplicatedTopic = streamsForTopics.stream()
            .collect(
                Collectors.groupingBy(
                    NewKafkaStreamForTopic::topic,
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

        final var streamsByTopics = streamsForTopics.stream()
            .collect(
                Collectors.toMap(
                    NewKafkaStreamForTopic::topic,
                    i -> i.factory().make(streamsBuilder)
                )
            );

        final var streamConfiguratorsByTopics = streamConfigurators.stream()
            .collect(
                Collectors.groupingBy(
                    StreamConnectorForTopic::topic,
                    Collectors.toList()
                )
            );

        for (final var streamConfiguratorsForTopic : streamConfiguratorsByTopics.entrySet()) {

            final var topic = streamConfiguratorsForTopic.getKey();
            final var configurators = streamConfiguratorsForTopic.getValue();

            final var customStream = streamsByTopics.get(topic);
            final var stream = customStream != null ? customStream : getDefaultStreamFactory.run(topic).make(streamsBuilder);

            if (configurators == null || configurators.isEmpty()) {
                continue;
            }

            for (final var configuratorForTopic : configurators) {
                configuratorForTopic.configurator().connect(stream);
            }
        }
    }
}
