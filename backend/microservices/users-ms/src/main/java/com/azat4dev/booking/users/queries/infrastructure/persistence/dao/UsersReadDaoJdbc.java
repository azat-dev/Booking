package com.azat4dev.booking.users.queries.infrastructure.persistence.dao;

import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.records.UserRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UsersReadDaoJdbc implements UsersReadDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mapper personalUserInfoRowMapper;

    public UsersReadDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.personalUserInfoRowMapper = new Mapper(objectMapper);
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
            log.atError().setCause(e).addKeyValue("userId", userId)
                .log("User not found: {}", userId);
            return Optional.empty();
        }
    }

    @RequiredArgsConstructor
    private static class Mapper implements RowMapper<UserRecord> {

        private final ObjectMapper objectMapper;

        @Override
        public UserRecord mapRow(ResultSet rs, int rowNum) throws SQLException {

            final var encodedPhoto = rs.getString("photo");
            final Optional<UserRecord.PhotoPath> photo;
            try {
                photo = encodedPhoto == null ? Optional.empty() : Optional.of(objectMapper.readValue(encodedPhoto, UserRecord.PhotoPath.class));
            } catch (JsonProcessingException e) {
                log.atError().log("Failed to parse photo: {}", encodedPhoto);
                throw new RuntimeException(e);
            }

            return new UserRecord(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("email"),
                    UserRecord.EmailVerificationStatus.valueOf(rs.getString("email_verification_status")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    photo
            );
        }
    }
}