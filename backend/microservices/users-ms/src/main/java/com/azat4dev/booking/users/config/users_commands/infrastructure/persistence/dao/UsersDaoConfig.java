package com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.dao;

import com.azat4dev.booking.users.commands.infrastructure.persistence.dao.UsersDao;
import com.azat4dev.booking.users.commands.infrastructure.persistence.dao.UsersDaoJdbc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class UsersDaoConfig {

    @Bean
    UsersDao usersDao(
        ObjectMapper objectMapper,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new UsersDaoJdbc(objectMapper, jdbcTemplate);
    }
}
