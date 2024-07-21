package com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepositoryImpl;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Configuration
public class OutboxEventsRepositoryConfig {

    private final DomainEventSerializer domainEventSerializer;
    private final OutboxEventsDao outboxEventsDao;
    private final DomainEventsFactory domainEventsFactory;
    private final List<Class<DomainEventPayload>> eventClasses;

    @Bean
    OutboxEventsRepository outboxEventsRepository() {

        final var classesByNames = eventClasses.stream()
            .collect(Collectors.toMap(Class::getSimpleName, c -> c));

        return new OutboxEventsRepositoryImpl(
            domainEventSerializer,
            outboxEventsDao,
            domainEventsFactory,
            eventType -> {
                final var clazz = classesByNames.get(eventType);

                log.atError()
                    .addArgument(eventType)
                    .log("Event type {} not found");

                return clazz;
            }
        );
    }
}