package com.azat4dev.demobooking.users.users_queries.data.dao;

import com.azat4dev.demobooking.users.users_queries.data.dao.records.UserRecord;
import com.azat4dev.demobooking.users.users_queries.domain.entities.PersonalUserInfo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UsersReadDaoJdbc implements UsersReadDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<UserRecord> personalUserInfoRowMapper = (rs, rowNum) -> new UserRecord(
        UUID.fromString(rs.getString("id")),
        rs.getString("email"),
        rs.getString("first_name"),
        rs.getString("last_name")
    );

    public UsersReadDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserRecord> getById(UUID userId) {

        final var sql = """
                SELECT *
                FROM users
                WHERE id = :userId
            """;

        try {
            final var foundUser = jdbcTemplate.queryForObject(
                sql,
                Map.of("userId", userId),
                personalUserInfoRowMapper
            );

            return Optional.ofNullable(foundUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}