package com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventSerializer;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventSerializerJSON;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepositoryImpl;
import com.azat4dev.booking.shared.data.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class OutboxEventsRepositoryConfig {

    private final ObjectMapper objectMapper;
    private final MapAnyDomainEvent mapEvent;
    private final OutboxEventsDao outboxEventsDao;
    private final DomainEventsFactory domainEventsFactory;

    private final Map<String, Class<?>> dtoClassesByNames;

    public OutboxEventsRepositoryConfig(
        List<MapDomainEvent<?, ?>> domainEventsMappers,
        ObjectMapper objectMapper,
        MapAnyDomainEvent mapEvent,
        OutboxEventsDao outboxEventsDao,
        DomainEventsFactory domainEventsFactory
    ) {

        this.objectMapper = objectMapper;
        this.mapEvent = mapEvent;
        this.outboxEventsDao = outboxEventsDao;
        this.domainEventsFactory = domainEventsFactory;

        this.dtoClassesByNames = domainEventsMappers.stream()
            .collect(Collectors.toMap(v -> v.getOriginalClass().getSimpleName(),
                MapDomainEvent::getSerializedClass
            ));
    }

    @Bean
    OutboxEventSerializer eventSerializer() {

        return new OutboxEventSerializerJSON(
            objectMapper,
            mapEvent,
            eventType -> {

                final var clazz = dtoClassesByNames.get(eventType);

                if (clazz == null) {
                    log.atError()
                        .addArgument(eventType)
                        .log("Event type {} not found");
                }

                return clazz;
            });
    }

    @Bean
    OutboxEventsRepository outboxEventsRepository(OutboxEventSerializer serializer) {
        return new OutboxEventsRepositoryImpl(
            serializer,
            outboxEventsDao,
            domainEventsFactory
        );
    }
}
