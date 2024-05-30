package com.azat4dev.booking.listingsms.queries.application.config.domain;

import com.azat4dev.booking.listingsms.queries.domain.entities.UserListingsFactory;
import com.azat4dev.booking.listingsms.queries.domain.entities.UserListingsFactoryImpl;
import com.azat4dev.booking.listingsms.queries.domain.entities.Users;
import com.azat4dev.booking.listingsms.queries.domain.entities.UsersImpl;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesDomainConfig")
public class DomainConfig {

    @Bean
    Users users(UserListingsFactory userListingsFactory) {
        return new UsersImpl(userListingsFactory);
    }

    @Bean
    UserListingsFactory userListingFactory(PrivateListingsReadRepository repository) {
        return new UserListingsFactoryImpl(repository);
    }
}
