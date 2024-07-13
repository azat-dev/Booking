package com.azat4dev.booking.users.commands.infrastructure.persistence.dao;

import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.commands.infrastructure.entities.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Observed
@Slf4j
@RequiredArgsConstructor
public class UsersDaoJdbc implements UsersDao {

    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mapper rowMapper;

    public UsersDaoJdbc(ObjectMapper objectMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new Mapper(objectMapper);
    }

    @Override
    public void addNew(UserData userData) {
        try {
            jdbcTemplate.update(
                """
                        INSERT INTO users (id, created_at, created_at_nano, updated_at, updated_at_nano, email, password, first_name, last_name, email_verification_status, photo)
                        VALUES (:id, :created_at, :created_at_nano, :updated_at, :updated_at_nano, :email, :password, :first_name, :last_name, :email_verification_status, :photo::jsonb)
                    """,
                userDataToParams(userData)
            );

            log.atInfo()
                .addKeyValue("userId", userData::id)
                .addArgument(userData::id)
                .log("User added: {}");

        } catch (DuplicateKeyException e) {
            log.atInfo().setCause(e).addKeyValue("userId", userData::id).log("User already exists");
            throw new Exception.UserAlreadyExists();
        } catch (JsonProcessingException e) {
            log.atError().setCause(e).log("Wrong JSON format");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserData> findByEmail(String email) {

        try {
            final var foundUser = jdbcTemplate.queryForObject(
                """
                        SELECT * FROM users WHERE email = :email LIMIT 1
                    """,
                Map.of("email", email),
                rowMapper
            );

            final var result = Optional.ofNullable(foundUser);
            log.atInfo().log("User found by email");
            return result;
        } catch (EmptyResultDataAccessException e) {
            log.atInfo().log("User not found by email");
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserData> findById(UUID id) {
        try {
            final var foundUser = jdbcTemplate.queryForObject(
                """
                        SELECT * FROM users WHERE id = :id LIMIT 1
                    """,
                Map.of("id", id),
                rowMapper
            );

            final var result = Optional.ofNullable(foundUser);
            log.atDebug().addKeyValue("id", id).log("User found by id");
            return result;
        } catch (EmptyResultDataAccessException e) {
            log.atDebug().addKeyValue("userId", id).log("User not found by id");
            return Optional.empty();
        }
    }

    Map<String, Object> userDataToParams(UserData userData) throws JsonProcessingException {
        final String encodedPhoto;

        if (userData.photo().isPresent()) {
            encodedPhoto = objectMapper.writeValueAsString(userData.photo().get());
        } else {
            encodedPhoto = null;
        }

        final var params = new HashMap<String, Object>();
        params.put("id", userData.id());
        params.put("created_at", Timestamp.valueOf(userData.createdAt().withNano(0)));
        params.put("created_at_nano", userData.createdAt().getNano());
        params.put("updated_at", Timestamp.valueOf(userData.updatedAt().withNano(0)));
        params.put("updated_at_nano", userData.updatedAt().getNano());
        params.put("email", userData.email());
        params.put("password", userData.encodedPassword());
        params.put("first_name", userData.firstName());
        params.put("last_name", userData.lastName());
        params.put("email_verification_status", userData.emailVerificationStatus().name());
        params.put("photo", encodedPhoto);

        return params;
    }

    @Override
    public void update(UserData userData) throws Exception.UserNotFound {
        final var sql = """
            UPDATE users SET
                created_at = :created_at,
                created_at_nano = :created_at_nano,
                updated_at = :updated_at,
                updated_at_nano = :updated_at_nano,
                email = :email,
                password = :password,
                first_name = :first_name,
                last_name = :last_name,
                email_verification_status = :email_verification_status,
                photo = :photo::jsonb
            WHERE id = :id
            """;

        try {

            final var params = userDataToParams(userData);
            final var numberOfUpdatedRecords = jdbcTemplate.update(sql, params);

            if (numberOfUpdatedRecords == 0) {
                log.atInfo().log("User not found");
                throw new Exception.UserNotFound();
            }
        } catch (JsonProcessingException e) {
            log.atError().setCause(e).log("Wrong JSON format");
        }

    }

    @RequiredArgsConstructor
    private static final class Mapper implements RowMapper<UserData> {

        private final ObjectMapper objectMapper;

        @Override
        public UserData mapRow(ResultSet rs, int rowNum) throws SQLException {
            final var encodedPhoto = rs.getString("photo");
            final Optional<UserData.PhotoPath> photo;
            try {
                photo = encodedPhoto == null ? Optional.empty() : Optional.of(objectMapper.readValue(encodedPhoto, UserData.PhotoPath.class));
            } catch (JsonProcessingException e) {
                log.atError().setCause(e).log("Wrong JSON format");
                throw new RuntimeException(e);
            }

            return new UserData(
                UUID.fromString(rs.getString("id")),
                rs.getTimestamp("created_at").toLocalDateTime()
                    .withNano(rs.getInt("created_at_nano")),
                rs.getTimestamp("updated_at").toLocalDateTime()
                    .withNano(rs.getInt("updated_at_nano")),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                EmailVerificationStatus.valueOf(rs.getString("email_verification_status")),
                photo
            );
        }
    }
}
