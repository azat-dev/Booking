package com.azat4dev.demobooking.users.users_queries.application.config;

import com.azat4dev.demobooking.users.users_queries.data.dao.UsersReadDao;
import com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories.MapUserRecordToPersonalInfo;
import com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories.MapUserRecordToPersonalInfoImpl;
import com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories.UsersReadRepository;
import com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories.UsersReadRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("usersQueriesDataConfig")
public class UsersDataConfig {

    @Bean
    public UsersReadRepository usersReadRepository(UsersReadDao usersReadDao, MapUserRecordToPersonalInfo mapUserRecordToPersonalInfo) {
        return new UsersReadRepositoryImpl(usersReadDao, mapUserRecordToPersonalInfo);
    }

    @Bean
    public MapUserRecordToPersonalInfo mapUserRecordToPersonalInfo() {
        return new MapUserRecordToPersonalInfoImpl();
    }
}
