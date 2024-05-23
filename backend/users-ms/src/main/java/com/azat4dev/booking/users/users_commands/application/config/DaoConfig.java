package com.azat4dev.booking.users.users_commands.application.config;

import com.azat4dev.booking.users.users_commands.data.repositories.dao.UsersDao;
import com.azat4dev.booking.users.users_commands.data.repositories.dao.UsersDaoJdbc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DaoConfig {
    @Bean
    UsersDao usersDao(
        ObjectMapper objectMapper,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new UsersDaoJdbc(objectMapper, jdbcTemplate);
    }
}
