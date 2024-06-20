package com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.dao;

import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDaoJdbc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration("commandsOutboxDaoConfig")
public class OutboxDaoConfig {

    @Bean
    OutboxEventsDao outboxEventsDao(
        ObjectMapper objectMapper,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new OutboxEventsDaoJdbc(jdbcTemplate);
    }
}
