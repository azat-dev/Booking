package com.azat4dev.booking.users.users_queries.application.config;

import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.users.users_queries.data.dao.UsersReadDao;
import com.azat4dev.booking.users.users_queries.data.repositories.MapToUserPhotoImpl;
import com.azat4dev.booking.users.users_queries.domain.interfaces.repositories.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration("usersQueriesDataConfig")
public class UsersDataConfig {

    @Bean
    public UsersReadRepository usersReadRepository(UsersReadDao usersReadDao, MapUserRecordToPersonalInfo mapUserRecordToPersonalInfo) {
        return new UsersReadRepositoryImpl(usersReadDao, mapUserRecordToPersonalInfo);
    }

    @Bean("readUsersPhotoBaseUrl")
    BaseUrl usersPhotoBaseUrl(
        @Value("${app.objects_storage.bucket.users-photo.endpoint}")
        URL url
    ) throws BaseUrl.Exception.WrongFormatException {
        return BaseUrl.checkAndMakeFrom(url);
    }

    @Bean
    MapToUserPhoto mapToUserPhoto(
        @Qualifier("readUsersPhotoBaseUrl")
        BaseUrl usersPhotoBaseUrl
    ) {
        return new MapToUserPhotoImpl(usersPhotoBaseUrl);
    }

    @Bean
    public MapUserRecordToPersonalInfo mapUserRecordToPersonalInfo(MapToUserPhoto mapToUserPhoto) {
        return new MapUserRecordToPersonalInfoImpl(mapToUserPhoto);
    }
}
