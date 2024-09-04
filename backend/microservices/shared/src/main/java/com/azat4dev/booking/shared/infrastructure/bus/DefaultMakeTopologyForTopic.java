package com.azat4dev.booking.shared.infrastructure.bus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
public class DefaultMakeTopologyForTopic implements KafkaMessageBus.MakeTopologyForTopic {

    private final Serde<Message> serde;

    @Override
    public Topology run(String topic, Optional<Set<String>> messageTypes, Consumer<Message> consumer) {
        final var sb = new StreamsBuilder();

        var stream = sb.stream(
            topic,
            Consumed.with(Serdes.String(), serde)
        );

        if (messageTypes.isPresent() && !messageTypes.get().isEmpty()) {
            stream = stream.filter((key, message) -> {
                if (messageTypes.isEmpty()) {
                    return false;
                }

                return messageTypes.get().contains(message.type());
            });
        }

        stream.foreach((k, message) -> {

            try {
                consumer.accept(message);
            } catch (Exception e) {
                log.atError()
                    .setCause(e)
                    .addArgument(topic)
                    .addArgument(message::id)
                    .addArgument(message::type)
                    .log("Error processing message: topic={} id={} type={}");

                throw e;
            }
        });

        return sb.build();
    }
}

