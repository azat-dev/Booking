package com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.UnitOfWorkImpl;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class UnitOfWorkConfig {

    @Bean
    UnitOfWorkFactory unitOfWorkFactory(
        TransactionTemplate transactionTemplate,
        OutboxEventsRepository outboxEventsRepository,
        UsersRepository usersRepository
    ) {

        return new UnitOfWorkFactory() {
            @Override
            public UnitOfWork make() {
                return new UnitOfWorkImpl(
                    transactionTemplate,
                    outboxEventsRepository,
                    usersRepository
                );
            }
        };
    }
}
