package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.users.users_commands.data.repositories.dao.UsersDao;
import com.azat4dev.demobooking.users.users_commands.data.repositories.dao.UsersDaoJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DaoConfig {
    @Bean
    UsersDao usersDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new UsersDaoJdbc(jdbcTemplate);
    }
}
