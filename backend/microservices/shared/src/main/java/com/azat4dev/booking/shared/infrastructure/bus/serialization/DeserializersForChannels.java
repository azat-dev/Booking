package com.azat4dev.booking.shared.infrastructure.bus.serialization;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeserializersForChannels {

    private final Map<String, MessageDeserializer> deserializersByChannels;

    public DeserializersForChannels(List<NewDeserializerForChannels> deserializers) {

        // Check channels must be unique
        final var channels = deserializers.stream()
            .flatMap(i -> i.channels().stream())
            .collect(Collectors.groupingBy(channel -> channel, Collectors.counting()));

        final var duplicatedChannel = channels.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .findFirst();


        duplicatedChannel.ifPresent(entry -> {
            throw new Exception.DuplicatedChannel(entry.getKey());
        });

        this.deserializersByChannels = deserializers.stream()
            .flatMap(i -> i.channels().stream().map(channel -> new DeserializerForChannel(channel, i.deserializer())))
            .collect(Collectors.toMap(DeserializerForChannel::channel, DeserializerForChannel::deserializer));
    }

    public Optional<MessageDeserializer> getForChannel(String channel) {
        return Optional.ofNullable(this.deserializersByChannels.get(channel));
    }

    private record DeserializerForChannel(String channel, MessageDeserializer deserializer) {
    }

    // Exceptions

    public static abstract class Exception extends RuntimeException {
        public static class DuplicatedChannel extends RuntimeException {
            public DuplicatedChannel(String channel) {
                super("Duplicated message deserializer for channel: " + channel);
            }
        }
    }
}
