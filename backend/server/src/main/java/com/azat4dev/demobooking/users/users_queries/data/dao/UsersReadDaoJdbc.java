package com.azat4dev.demobooking.users.users_queries.data.dao;

import com.azat4dev.demobooking.users.users_queries.data.dao.entities.PersonalUserInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class UsersReadDaoJdbc implements UsersReadDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<PersonalUserInfo> personalUserInfoRowMapper = (rs, rowNum) -> new PersonalUserInfo(
        UUID.fromString(rs.getString("id")),
        rs.getString("email"),
        rs.getString("first_name"),
        rs.getString("last_name")
    );

    public UsersReadDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PersonalUserInfo> getPersonalUserInfoById(UUID userId) {

        final var sql = """
                SELECT id, email, first_name, last_name
                FROM users
                WHERE id = :userId
                LIMIT 1
            """;

        final var foundUser = jdbcTemplate.queryForObject(
            sql,
            Map.of("userId", userId),
            personalUserInfoRowMapper
        );
        return Optional.ofNullable(foundUser);
    }
}