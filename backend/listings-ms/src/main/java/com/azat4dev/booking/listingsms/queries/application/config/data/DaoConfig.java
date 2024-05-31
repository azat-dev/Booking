package com.azat4dev.booking.listingsms.queries.application.config.data;

import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDaoJdbc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration("queriesDaoConfig")
public class DaoConfig {

    @Bean
    ListingsReadDao listingsReadDao(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        return new ListingsReadDaoJdbc(jdbcTemplate, objectMapper);
    }
}
