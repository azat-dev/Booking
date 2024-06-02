package com.azat4dev.booking.listingsms.commands.application.config.data;

import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.data.repositories.ListingsRepositoryImpl;
import com.azat4dev.booking.listingsms.commands.data.repositories.UnitOfWorkImpl;
import com.azat4dev.booking.listingsms.commands.data.repositories.mappers.MapListingToRecord;
import com.azat4dev.booking.listingsms.commands.data.repositories.mappers.MapListingToRecordImpl;
import com.azat4dev.booking.listingsms.commands.data.repositories.mappers.MapRecordToListing;
import com.azat4dev.booking.listingsms.commands.data.repositories.mappers.MapRecordToListingImpl;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepositoryImpl;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration("commandsDataConfig")
public class DataConfig {

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

    @Bean
    MapListingToRecord mapListingToData(ObjectMapper objectMapper) {
        return new MapListingToRecordImpl(objectMapper);
    }

    @Bean
    MapRecordToListing mapDataToListing(ObjectMapper objectMapper) {
        return new MapRecordToListingImpl(objectMapper);
    }

    @Bean
    ListingsRepository listingsRepository(
        ListingsDao listingsDao,
        TimeProvider timeProvider,
        MapListingToRecord mapListingToRecord,
        MapRecordToListing mapRecordToListing

    ) {
        return new ListingsRepositoryImpl(
            listingsDao,
            timeProvider,
            mapListingToRecord,
            mapRecordToListing
        );
    }

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }
}
