package com.azat4dev.booking.shared.infrastructure.bus.serialization;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageSerializersForTopics {

    private final Map<String, MessageSerializer> serializersByTopics;

    public MessageSerializersForTopics(List<CustomMessageSerializerForTopics> serializers) {

        // Check topics must be unique
        final var topics = serializers.stream()
            .flatMap(i -> i.topics().stream())
            .collect(Collectors.groupingBy(topic -> topic, Collectors.counting()));

        final var duplicatedTopic = topics.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .findFirst();


        duplicatedTopic.ifPresent(entry -> {
            throw new DuplicatedTopic(entry.getKey());
        });

        this.serializersByTopics = serializers.stream()
            .flatMap(i -> i.topics().stream().map(topic -> new SerializerForTopic(topic, i.serializer())))
            .collect(Collectors.toMap(SerializerForTopic::topic, SerializerForTopic::serializer));
    }

    public Optional<MessageSerializer> getForTopic(String topic) {
        return Optional.ofNullable(this.serializersByTopics.get(topic));
    }

    private record SerializerForTopic(String topic, MessageSerializer serializer) {
    }

    // Exceptions

    public static class DuplicatedTopic extends RuntimeException {
        public DuplicatedTopic(String topic) {
            super("Duplicated message serializer for topic: " + topic);
        }
    }
}
