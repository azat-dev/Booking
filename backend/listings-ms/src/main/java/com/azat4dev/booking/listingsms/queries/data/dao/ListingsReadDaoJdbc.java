package com.azat4dev.booking.listingsms.queries.data.dao;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public final class ListingsReadDaoJdbc implements ListingsReadDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mapper rowMapper = new Mapper();

    @Override
    public Optional<ListingRecord> findById(UUID listingId) {

        try {
            final var row = jdbcTemplate.queryForObject("SELECT * FROM listings WHERE id = :id LIMIT 1",
                Map.of("id", listingId),
                this.rowMapper
            );
            return Optional.ofNullable(row);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static final class Mapper implements RowMapper<ListingRecord> {

        @Override
        public ListingRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ListingRecord(
                UUID.fromString(rs.getString("id")),
                rs.getString("title"),
                Optional.ofNullable(rs.getString("description"))
            );
        }
    }
}
