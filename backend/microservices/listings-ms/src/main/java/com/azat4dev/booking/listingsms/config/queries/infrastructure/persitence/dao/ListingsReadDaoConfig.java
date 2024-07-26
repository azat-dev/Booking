package com.azat4dev.booking.listingsms.config.queries.infrastructure.persitence.dao;

import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.infrastructure.persistence.dao.ListingsReadDaoJooq;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDaoConfig")
public class ListingsReadDaoConfig {

    @Bean
    ListingsReadDao listingsReadDao(DSLContext dslContext) {
        return new ListingsReadDaoJooq(dslContext);
    }
}
