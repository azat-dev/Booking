package com.azat4dev.booking.users.users_commands.application.config.domain;

import com.azat4dev.booking.common.domain.AutoConnectCommandHandlersToBus;
import com.azat4dev.booking.shared.domain.core.UserIdFactory;
import com.azat4dev.booking.shared.domain.core.UserIdFactoryImpl;
import com.azat4dev.booking.shared.domain.event.*;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.data.KafkaDomainEventsBus;
import com.azat4dev.booking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.UsersImpl;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.users.users_commands.domain.producers.OutboxEventsPublisher;
import com.azat4dev.booking.users.users_commands.domain.producers.OutboxEventsPublisherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.function.Function;


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
    public DomainEventsBus domainEventsBus(
        KafkaTemplate<String, String> kafkaTemplate,
        DomainEventSerializer domainEventSerializer,
        Function<String, ConcurrentMessageListenerContainer<String, String>> containerFactory,
        TimeProvider timeProvider,
        EventIdGenerator eventIdGenerator
    ) {
        return new KafkaDomainEventsBus(
            kafkaTemplate,
            domainEventSerializer,
            containerFactory,
            timeProvider,
            eventIdGenerator
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
