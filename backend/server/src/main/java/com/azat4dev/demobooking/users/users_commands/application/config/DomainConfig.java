package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.DomainEventsBus;
import com.azat4dev.demobooking.common.EventIdGenerator;
import com.azat4dev.demobooking.common.RandomEventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.OutboxEventsPublisher;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.OutboxEventsPublisherImpl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.demobooking.users.users_commands.domain.services.UsersService;
import com.azat4dev.demobooking.users.users_commands.domain.services.UsersServiceImpl;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserIdFactory;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserIdFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
    public UsersService usersService(
        TimeProvider timeProvider,
        UnitOfWorkFactory unitOfWorkFactory,
        EventIdGenerator eventIdGenerator,
        OutboxEventsPublisher outboxEventsPublisher
    ) {
        return new UsersServiceImpl(
            timeProvider,
            eventIdGenerator,
            outboxEventsPublisher::publishEvents,
            unitOfWorkFactory
        );
    }

    @Bean
    DomainEventsBus domainEventsBus() {
        return new DomainEventsBus() {
            @Override
            public void publish(DomainEvent<?> event) {
                LOGGER.info("Publishing event: {}", event);
            }
        };
    }

    @Bean
    OutboxEventsPublisher outboxEventsPublisher(
        OutboxEventsRepository outboxEventsRepository,
        DomainEventsBus domainEventsBus
    ) {
        return new OutboxEventsPublisherImpl(outboxEventsRepository, domainEventsBus);
    }
}
