package com.azat4dev.booking.listingsms.queries.application.config.application;

import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandler;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandlerImpl;
import com.azat4dev.booking.listingsms.queries.domain.entities.UserListings;
import com.azat4dev.booking.listingsms.queries.domain.entities.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("queriesCommandHandlersConfig")
public class CommandHandlersConfig {

    @Bean
    GetListingPrivateDetailsHandler getListingPrivateDetailsHandler(Users users) {
        return new GetListingPrivateDetailsHandlerImpl(users);
    }
}
