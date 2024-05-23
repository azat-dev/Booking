package com.azat4dev.booking.users.users_commands.application.config.domain;

import com.azat4dev.booking.common.domain.annotations.CommandHandlerBean;
import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.shared.domain.event.DomainEventsFactory;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.GenerateUserPhotoObjectName;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.GenerateUserPhotoObjectNameImpl;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateUserPhotoConfig {

    @CommandHandlerBean
    UpdateUserPhotoHandler updateUserPhotoHandler(
        DomainEventsFactory domainEventsFactory,
        @Qualifier("usersPhotoBucket")
        MediaObjectsBucket userPhotoBucket,
        UnitOfWorkFactory unitOfWorkFactory,
        DomainEventsBus bus
    ) {
        return new UpdateUserPhotoHandler(userPhotoBucket, unitOfWorkFactory, bus);
    }

    @Bean
    GenerateUserPhotoUploadUrlHandler generateUserPhotoUploadUrlHandler(
        @Value("${app.objects_storage.bucket.users-photo.upload-url.expires-in-seconds}")
        int expireInSeconds,
        @Qualifier("usersPhotoBucket")
        MediaObjectsBucket userPhotoBucket,
        GenerateUserPhotoObjectName generateUserPhotoObjectName,
        DomainEventsBus bus
    ) {
        return new GenerateUserPhotoUploadUrlHandler(
            expireInSeconds,
            generateUserPhotoObjectName,
            userPhotoBucket,
            bus
        );
    }

    @Bean
    GenerateUserPhotoObjectName generateUserPhotoObjectName(TimeProvider timeProvider) {
        return new GenerateUserPhotoObjectNameImpl(timeProvider);
    }
}
