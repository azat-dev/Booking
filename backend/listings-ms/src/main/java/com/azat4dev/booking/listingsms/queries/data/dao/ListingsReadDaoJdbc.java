package com.azat4dev.booking.listingsms.queries.data.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private final Mapper rowMapper;

    public ListingsReadDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.rowMapper = new Mapper(objectMapper);
    }

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
            "SELECT * FROM listings WHERE owner_id = :owner_id ORDER BY title ASC",
            Map.of("owner_id", ownerId),
            this.rowMapper
        );
    }

    @AllArgsConstructor
    private static final class Mapper implements RowMapper<ListingRecord> {

        private final ObjectMapper objectMapper;

        private Optional<ListingRecord.Address> mapAddress(ResultSet rs) throws SQLException {
            final var country = rs.getString("address_country");
            if (country == null) {
                return Optional.empty();
            }
            return Optional.of(new ListingRecord.Address(
                country,
                rs.getString("address_city"),
                rs.getString("address_street")
            ));
        }

        private ListingRecord.GuestsCapacity mapGuestsCapacity(ResultSet rs) throws SQLException {
            return new ListingRecord.GuestsCapacity(
                rs.getInt("guests_capacity_adults"),
                rs.getInt("guests_capacity_children"),
                rs.getInt("guests_capacity_infants")
            );
        }

        private List<ListingRecord.Photo> mapPhotos(ResultSet rs) throws SQLException {
            final var rawPhotos = rs.getString("photos");
            if (rawPhotos == null) {
                return List.of();
            }

            try {
                return List.of(objectMapper.readValue(rawPhotos, ListingRecord.Photo[].class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

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
                Optional.ofNullable(rs.getString("description")),
                mapGuestsCapacity(rs),
                Optional.ofNullable(rs.getString("property_type")),
                Optional.ofNullable(rs.getString("room_type")),
                mapAddress(rs),
                mapPhotos(rs)
            );
        }
    }
}
