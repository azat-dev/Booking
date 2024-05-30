package com.azat4dev.booking.listingsms.queries.data.dao;

import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
            final var row = jdbcTemplate.queryForObject(
                "SELECT * FROM listings WHERE id = :id LIMIT 1",
                Map.of("id", listingId),
                this.rowMapper
            );
            return Optional.ofNullable(row);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ListingRecord> findAllByOwnerId(UUID ownerId) {
        return jdbcTemplate.query(
            "SELECT * FROM listings WHERE owner_id = :owner_id",
            Map.of("owner_id", ownerId),
            this.rowMapper
        );
    }

    private static final class Mapper implements RowMapper<ListingRecord> {

        @Override
        public ListingRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ListingRecord(
                UUID.fromString(rs.getString("id")),
                rs.getTimestamp("created_at").toLocalDateTime()
                    .withNano(rs.getInt("created_at_nano")),
                rs.getTimestamp("updated_at").toLocalDateTime()
                    .withNano(rs.getInt("updated_at_nano")),
                UUID.fromString(rs.getString("owner_id")),
                rs.getString("title"),
                rs.getString("status"),
                Optional.ofNullable(rs.getString("description"))
            );
        }
    }
}
