package com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepositoryImpl;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxEventsRepositoryConfig {

    @Bean
    OutboxEventsRepository outboxEventsRepository(
        DomainEventSerializer domainEventSerializer,
        OutboxEventsDao outboxEventsDao,
        DomainEventsFactory domainEventsFactory
    ) {
        return new OutboxEventsRepositoryImpl(
            domainEventSerializer,
            outboxEventsDao,
            domainEventsFactory
        );
    }
}
