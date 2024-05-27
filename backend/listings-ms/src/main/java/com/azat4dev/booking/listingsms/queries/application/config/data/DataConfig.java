package com.azat4dev.booking.listingsms.queries.application.config.data;

import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.data.repositories.PrivateListingsReadRepositoryImpl;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    public PrivateListingsReadRepository dataConfig(ListingsReadDao dao) {
        return new PrivateListingsReadRepositoryImpl(dao);
    }
}
