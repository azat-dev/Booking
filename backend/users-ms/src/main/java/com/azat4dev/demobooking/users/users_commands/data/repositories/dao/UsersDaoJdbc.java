package com.azat4dev.demobooking.users.users_commands.data.repositories.dao;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public final class UsersDaoJdbc implements UsersDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<UserData> rowMapper = (rs, rowNum) -> new UserData(
        UUID.fromString(rs.getString("id")),
        rs.getTimestamp("created_at").toLocalDateTime()
            .withNano(rs.getInt("created_at_nano")),
        rs.getTimestamp("updated_at").toLocalDateTime()
            .withNano(rs.getInt("updated_at_nano")),
        rs.getString("email"),
        rs.getString("password"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        EmailVerificationStatus.valueOf(rs.getString("email_verification_status"))
    );

    @Override
    public void addNew(UserData userData) {
        try {
            jdbcTemplate.update(
                """
                        INSERT INTO users (id, created_at, created_at_nano, updated_at, updated_at_nano, email, password, first_name, last_name, email_verification_status)
                        VALUES (:id, :created_at, :created_at_nano, :updated_at, :updated_at_nano, :email, :password, :first_name, :last_name, :email_verification_status)
                    """,
                Map.of(
                    "id", userData.id(),
                    "created_at", Timestamp.valueOf(userData.createdAt().withNano(0)),
                    "created_at_nano", userData.createdAt().getNano(),
                    "updated_at", Timestamp.valueOf(userData.updatedAt().withNano(0)),
                    "updated_at_nano", userData.updatedAt().getNano(),
                    "email", userData.email(),
                    "password", userData.encodedPassword(),
                    "first_name", userData.firstName(),
                    "last_name", userData.lastName(),
                    "email_verification_status", userData.emailVerificationStatus().name()
                )
            );
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException();
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

            return Optional.ofNullable(foundUser);
        } catch (EmptyResultDataAccessException e) {
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

            return Optional.ofNullable(foundUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
