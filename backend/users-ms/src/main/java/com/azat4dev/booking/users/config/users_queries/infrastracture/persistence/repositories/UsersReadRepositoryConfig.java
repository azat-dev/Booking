package com.azat4dev.booking.users.config.users_queries.infrastracture.persistence.repositories;

import com.azat4dev.booking.users.config.users_commands.properties.UsersPhotoBucketConfigProperties;
import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.UsersReadDao;
import com.azat4dev.booking.users.queries.infrastructure.persistence.repositories.MapToUserPhotoImpl;
import com.azat4dev.booking.users.queries.domain.interfaces.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration("usersQueriesDataConfig")
public class UsersReadRepositoryConfig {

    private final UsersPhotoBucketConfigProperties bucketConfig;

    @Bean
    public UsersReadRepository usersReadRepository(UsersReadDao usersReadDao, MapUserRecordToPersonalInfo mapUserRecordToPersonalInfo) {
        return new UsersReadRepositoryImpl(usersReadDao, mapUserRecordToPersonalInfo);
    }

    @Bean
    MapToUserPhoto mapToUserPhoto() {
        return new MapToUserPhotoImpl(bucketConfig.getBaseUrl());
    }

    @Bean
    public MapUserRecordToPersonalInfo mapUserRecordToPersonalInfo(MapToUserPhoto mapToUserPhoto) {
        return new MapUserRecordToPersonalInfoImpl(mapToUserPhoto);
    }
}
