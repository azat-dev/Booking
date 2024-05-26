package com.azat4dev.booking.listingsms.application.config.data;

import com.azat4dev.booking.listingsms.data.dao.ListingsDao;
import com.azat4dev.booking.listingsms.data.dao.ListingsDaoJDBC;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class ListingsDaoConfig {

    @Bean
    ListingsDao listingsDao(
        ObjectMapper objectMapper,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new ListingsDaoJDBC(objectMapper, jdbcTemplate);
    }
}
