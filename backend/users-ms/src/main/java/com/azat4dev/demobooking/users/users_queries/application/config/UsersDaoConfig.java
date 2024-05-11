package com.azat4dev.demobooking.users.users_queries.application.config;

import com.azat4dev.demobooking.users.users_queries.data.dao.UsersReadDao;
import com.azat4dev.demobooking.users.users_queries.data.dao.UsersReadDaoJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration("usersQueriesDaoConfig")
public class UsersDaoConfig {

    @Bean
    public UsersReadDao usersReadDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new UsersReadDaoJdbc(jdbcTemplate);
    }
}
