package com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.dao;

import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDaoJdbc;
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
