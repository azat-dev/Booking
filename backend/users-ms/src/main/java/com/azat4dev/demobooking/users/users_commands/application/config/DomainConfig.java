package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.common.*;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.data.KafkaDomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.OutboxEventsPublisher;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.OutboxEventsPublisherImpl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.demobooking.users.users_commands.domain.services.UsersService;
import com.azat4dev.demobooking.users.users_commands.domain.services.UsersServiceImpl;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserIdFactory;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserIdFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;


@Configuration
public class DomainConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(DomainConfig.class);

    @Bean
    public UserIdFactory userIdFactory() {
        return new UserIdFactoryImpl();
    }

    @Bean
    EventIdGenerator eventIdGenerator() {
        return new RandomEventIdGenerator();
    }

    @Bean
    DomainEventsFactory domainEventsFactory(
        EventIdGenerator eventIdGenerator,
        TimeProvider timeProvider
    ) {
        return new DomainEventsFactoryImpl(eventIdGenerator, timeProvider);
    }

    @Bean
    public UsersService usersService(
        TimeProvider timeProvider,
        UnitOfWorkFactory unitOfWorkFactory,
        OutboxEventsPublisher outboxEventsPublisher,
        DomainEventsFactory domainEventsFactory
    ) {
        return new UsersServiceImpl(
            timeProvider,
            outboxEventsPublisher::publishEvents,
            unitOfWorkFactory,
            domainEventsFactory
        );
    }

    @Bean
    public DomainEventsBus domainEventsBus(
        KafkaTemplate<String, String> kafkaTemplate,
        DomainEventSerializer domainEventSerializer
    ) {
        return new KafkaDomainEventsBus(
            kafkaTemplate,
            domainEventSerializer
        );
    }

    @Bean
    public OutboxEventsPublisher outboxEventsPublisher(
        OutboxEventsRepository outboxEventsRepository,
        DomainEventsBus domainEventsBus
    ) {
        return new OutboxEventsPublisherImpl(outboxEventsRepository, domainEventsBus);
    }
}
