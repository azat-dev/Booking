package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public final class KafkaDomainEventsBus implements DomainEventsBus {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DomainEventSerializer domainEventSerializer;

    @Override
    public void publish(DomainEventNew<?> event) {

        kafkaTemplate.send(
            event.payload().getClass().getSimpleName(),
            domainEventSerializer.serialize(event)
        );
    }
}