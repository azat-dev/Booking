package com.azat4dev.booking.users.config.users_commands.domain;

import com.azat4dev.booking.common.domain.AutoConnectCommandHandlersToBus;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.domain.events.*;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.values.user.UserIdFactory;
import com.azat4dev.booking.shared.domain.values.user.UserIdFactoryImpl;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.commands.domain.handlers.users.UsersImpl;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsPublisher;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsPublisherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@AutoConnectCommandHandlersToBus
@Configuration
public class DomainConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainConfig.class);

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
    public Users usersService(
        TimeProvider timeProvider,
        UnitOfWorkFactory unitOfWorkFactory,
        OutboxEventsPublisher outboxEventsPublisher
    ) {
        return new UsersImpl(
            timeProvider,
            outboxEventsPublisher::publishEvents,
            unitOfWorkFactory
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
