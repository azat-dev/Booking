package com.azat4dev.booking.shared.infrastructure.bus.serialization;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SerializersForChannels {

    private final Map<String, MessageSerializer> serializersByChannels;

    public SerializersForChannels(List<NewSerializerForChannels> serializers) {

        // Check channels must be unique
        final var channels = serializers.stream()
            .flatMap(i -> i.channels().stream())
            .collect(Collectors.groupingBy(channel -> channel, Collectors.counting()));

        final var duplicatedChannel = channels.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .findFirst();


        duplicatedChannel.ifPresent(entry -> {
            throw new Exception.DuplicatedChannel(entry.getKey());
        });

        this.serializersByChannels = serializers.stream()
            .flatMap(i -> i.channels().stream().map(channel -> new SerializerForChannel(channel, i.serializer())))
            .collect(Collectors.toMap(SerializerForChannel::channel, SerializerForChannel::serializer));
    }

    public Optional<MessageSerializer> getForChannel(String channel) {
        return Optional.ofNullable(this.serializersByChannels.get(channel));
    }

    private record SerializerForChannel(String channel, MessageSerializer serializer) {
    }

    // Exceptions

    public static abstract class Exception extends RuntimeException {
        public static class DuplicatedChannel extends RuntimeException {
            public DuplicatedChannel(String channel) {
                super("Duplicated message serializer for channel: " + channel);
            }
        }
    }
}
