package com.azat4dev.booking.users.users_commands.application.config.domain;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.photo.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateUserPhotoConfig {

    @Bean
    SetNewPhotoForUser updateUserPhoto(
        @Qualifier("usersPhotoBucket")
        MediaObjectsBucket userPhotoBucket,
        Users users,
        DomainEventsBus bus
    ) {
        return new SetNewPhotoForUserImpl(userPhotoBucket, users, bus);
    }

    @Bean
    GenerateUserPhotoObjectName generateUserPhotoObjectName(TimeProvider timeProvider) {
        return new GenerateUserPhotoObjectNameImpl(timeProvider);
    }

    @Bean
    GenerateUrlForUploadUserPhoto generateUrlForUploadUserPhoto(
        @Value("${app.objects_storage.bucket.users-photo.upload-url.expires-in-seconds}")
        int expireInSeconds,
        @Qualifier("usersPhotoBucket")
        MediaObjectsBucket userPhotoBucket,
        GenerateUserPhotoObjectName generateUserPhotoObjectName,
        DomainEventsBus bus
    ) {
        return new GenerateUrlForUploadUserPhotoImpl(
            expireInSeconds,
            generateUserPhotoObjectName,
            userPhotoBucket,
            bus
        );
    }
}
