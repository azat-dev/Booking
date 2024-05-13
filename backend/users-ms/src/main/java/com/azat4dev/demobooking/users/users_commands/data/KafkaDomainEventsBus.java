package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;

import java.io.Closeable;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public final class KafkaDomainEventsBus implements DomainEventsBus {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DomainEventSerializer domainEventSerializer;
    private final Function<String, ConcurrentMessageListenerContainer<String, String>> containerFactory;

    @Override
    public void publish(DomainEventNew<?> event) {

        kafkaTemplate.send(
            event.payload().getClass().getSimpleName(),
            domainEventSerializer.serialize(event)
        );
    }

    @Override
    public Closeable listen(Class<DomainEventPayload> eventType, Consumer<DomainEventNew<?>> listener) {

        final var topic = eventType.getSimpleName();
        final var container = containerFactory.apply(topic);

        container.setupMessageListener(new AcknowledgingMessageListener<>() {

            @Override
            public void onMessage(ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment) {
                listener.accept(domainEventSerializer.deserialize((String) data.value()));
                if (acknowledgment != null) {
                    acknowledgment.acknowledge();
                }
            }
        });

        container.start();
        return () -> container.stop();
    }
}