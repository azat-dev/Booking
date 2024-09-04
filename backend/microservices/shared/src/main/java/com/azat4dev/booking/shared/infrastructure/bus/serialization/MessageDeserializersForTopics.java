package com.azat4dev.booking.shared.infrastructure.bus.serialization;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageDeserializersForTopics {

    private final Map<String, MessageDeserializer> deserializersByTopics;

    public MessageDeserializersForTopics(List<CustomMessageDeserializerForTopics> deserializers) {

        // Check topics must be unique
        final var topics = deserializers.stream()
            .flatMap(i -> i.topics().stream())
            .collect(Collectors.groupingBy(topic -> topic, Collectors.counting()));

        final var duplicatedTopic = topics.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .findFirst();


        duplicatedTopic.ifPresent(entry -> {
            throw new DuplicatedTopic(entry.getKey());
        });

        this.deserializersByTopics = deserializers.stream()
            .flatMap(i -> i.topics().stream().map(topic -> new DeserializerForTopic(topic, i.serializer())))
            .collect(Collectors.toMap(DeserializerForTopic::topic, DeserializerForTopic::deserializer));
    }

    public Optional<MessageDeserializer> getForTopic(String topic) {
        return Optional.ofNullable(this.deserializersByTopics.get(topic));
    }

    private record DeserializerForTopic(String topic, MessageDeserializer deserializer) {}

    // Exceptions

    public static class DuplicatedTopic extends RuntimeException {
        public DuplicatedTopic(String topic) {
            super("Duplicated message deserializer for topic: " + topic);
        }
    }
}
