package com.azat4dev.booking.shared.infrastructure.bus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomTopologyFactoriesForTopics {

    private final Map<String, KafkaMessageBus.MakeTopologyForTopic> topologiesForTopics = new HashMap<>();

    public CustomTopologyFactoriesForTopics(List<CustomTopologyForTopic> topologiesForTopics) {

        // Check topics must be unique
        final var topics = topologiesForTopics.stream()
            .map(CustomTopologyForTopic::topic)
            .collect(Collectors.groupingBy(topic -> topic, Collectors.counting()));

        final var duplicatedTopic = topics.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .findFirst();


        duplicatedTopic.ifPresent(entry -> {
            throw new DuplicatedTopic(entry.getKey());
        });

        topologiesForTopics.forEach(item -> {
            this.topologiesForTopics.put(item.topic(), item.makeTopologyForTopic());
        });
    }

    public Optional<KafkaMessageBus.MakeTopologyForTopic> getForTopic(String topic) {
        return Optional.ofNullable(this.topologiesForTopics.getOrDefault(topic, null));
    }

    // Exceptions

    public static class DuplicatedTopic extends RuntimeException {
        public DuplicatedTopic(String topic) {
            super("Duplicated topology factory for topic: " + topic);
        }
    }
}
