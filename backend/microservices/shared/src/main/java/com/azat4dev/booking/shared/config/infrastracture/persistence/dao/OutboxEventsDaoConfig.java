package com.azat4dev.booking.shared.config.infrastracture.persistence.dao;

import com.azat4dev.booking.shared.infrastructure.persistence.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.infrastructure.persistence.dao.outbox.OutboxEventsDaoJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class OutboxEventsDaoConfig {

    @Bean
    OutboxEventsDao outboxEventsDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new OutboxEventsDaoJdbc(jdbcTemplate);
    }
}
