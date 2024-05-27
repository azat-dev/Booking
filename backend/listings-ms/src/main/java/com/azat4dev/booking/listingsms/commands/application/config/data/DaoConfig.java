package com.azat4dev.booking.listingsms.commands.application.config.data;

import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDaoJDBC;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDaoJdbc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration("commandsDaoConfig")
public class DaoConfig {

    @Bean
    ListingsDao listingsDao(
        ObjectMapper objectMapper,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new ListingsDaoJDBC(objectMapper, jdbcTemplate);
    }

    @Bean
    OutboxEventsDao outboxEventsDao(
        ObjectMapper objectMapper,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new OutboxEventsDaoJdbc(jdbcTemplate);
    }
}
