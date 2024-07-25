package com.azat4dev.booking.shared.config.infrastracture.persistence.repositories.outbox;

import com.azat4dev.booking.shared.config.infrastracture.persistence.dao.OutboxEventsDaoConfig;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventSerializer;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepositoryImpl;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({OutboxEventsDaoConfig.class, JSONOutboxEventSerializerConfig.class})
@Configuration
@AllArgsConstructor
public class OutboxEventsRepositoryConfig {

    private final OutboxEventsDao outboxEventsDao;
    private final EventIdGenerator eventIdGenerator;
    private final TimeProvider timeProvider;

    @Bean
    OutboxEventsRepository outboxEventsRepository(OutboxEventSerializer serializer) {
        return new OutboxEventsRepositoryImpl(
            serializer,
            outboxEventsDao,
            eventIdGenerator,
            timeProvider
        );
    }
}
