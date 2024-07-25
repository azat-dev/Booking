package com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.infrastructure.repositories.UnitOfWorkImpl;
import com.azat4dev.booking.shared.config.infrastracture.persistence.repositories.outbox.OutboxEventsRepositoryConfig;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

@Import(OutboxEventsRepositoryConfig.class)
@Configuration
public class UnitOfWorkConfig {

    @Bean
    UnitOfWorkFactory unitOfWorkFactory(
        PlatformTransactionManager transactionManager,
        OutboxEventsRepository outboxEventsRepository,
        ListingsRepository listingsRepository
    ) {
        return new UnitOfWorkFactory() {
            @Override
            public UnitOfWork make() {
                return new UnitOfWorkImpl(
                    transactionManager,
                    outboxEventsRepository,
                    listingsRepository
                );
            }
        };
    }
}
