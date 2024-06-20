package com.azat4dev.booking.users.config.users_commands.domain;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.config.users_commands.properties.UsersPhotoBucketConfigProperties;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.commands.domain.handlers.users.photo.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class UpdateUserPhotoConfig {

    private final UsersPhotoBucketConfigProperties bucketConfig;

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
        @Qualifier("usersPhotoBucket")
        MediaObjectsBucket userPhotoBucket,
        GenerateUserPhotoObjectName generateUserPhotoObjectName,
        DomainEventsBus bus
    ) {
        return new GenerateUrlForUploadUserPhotoImpl(
            bucketConfig.getUploadUrlExpiresIn(),
            generateUserPhotoObjectName,
            userPhotoBucket,
            bus
        );
    }
}
