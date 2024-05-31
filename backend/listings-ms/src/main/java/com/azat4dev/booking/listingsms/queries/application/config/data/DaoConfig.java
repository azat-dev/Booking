package com.azat4dev.booking.listingsms.queries.application.config.data;

import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDaoJooq;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDaoConfig")
public class DaoConfig {

    @Bean
    ListingsReadDao listingsReadDao(DSLContext dslContext) {
        return new ListingsReadDaoJooq(dslContext);
    }
}
