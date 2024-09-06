package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SimpleKafkaStreamConfigurator implements KafkaStreamConfigurator {

    private final String topic;
    private final List<MessageListener> listeners;

    private void addListener(KStream<String, Message> stream, MessageListener messageListener) {

        stream.foreach((k, message) -> {
            if (messageListener.messageTypes().isPresent()) {
                if (!messageListener.messageTypes().get().contains(message.type())) {
                    return;
                }
            }

            try {
                messageListener.consume(message);
            } catch (java.lang.Exception e) {
                log.atError()
                    .setCause(e)
                    .addArgument(topic)
                    .addArgument(message::id)
                    .addArgument(message::type)
                    .log("Error processing message: topic={} id={} type={}");

                throw e;
            }
        });
    }

    @Override
    public void configure(KStream<String, Message> stream) {
        listeners.forEach(listener -> {
            addListener(stream, listener);
        });
    }
}
