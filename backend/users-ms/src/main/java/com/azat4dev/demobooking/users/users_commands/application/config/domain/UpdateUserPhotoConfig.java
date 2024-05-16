package com.azat4dev.demobooking.users.users_commands.application.config.domain;

import com.azat4dev.demobooking.common.domain.annotations.CommandHandlerBean;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.GenerateUserPhotoObjectName;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.GenerateUserPhotoObjectNameImpl;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateUserPhotoConfig {

    @CommandHandlerBean
    UpdateUserPhotoHandler updateUserPhotoHandler(
        DomainEventsFactory domainEventsFactory
    ) {
        return null;
    }

    @Bean
    GenerateUserPhotoObjectName generateUserPhotoObjectName(TimeProvider timeProvider) {
        return new GenerateUserPhotoObjectNameImpl(timeProvider);
    }
}
