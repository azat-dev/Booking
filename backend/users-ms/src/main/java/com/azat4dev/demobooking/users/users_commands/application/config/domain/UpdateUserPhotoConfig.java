package com.azat4dev.demobooking.users.users_commands.application.config.domain;

import com.azat4dev.demobooking.common.domain.annotations.CommandHandlerBean;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateUserPhotoConfig {

    @CommandHandlerBean
    UpdateUserPhotoHandler updateUserPhotoHandler(
        DomainEventsFactory domainEventsFactory
    ) {
        return null;
    }
}
