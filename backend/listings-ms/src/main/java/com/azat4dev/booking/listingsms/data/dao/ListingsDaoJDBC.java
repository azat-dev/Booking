package com.azat4dev.booking.listingsms.data.dao;

import com.azat4dev.booking.listingsms.data.dao.entities.ListingData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

@AllArgsConstructor
public final class ListingsDaoJDBC implements ListingsDao {

    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mapper rowMapper;

    public ListingsDaoJDBC(ObjectMapper objectMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new Mapper(objectMapper);
    }

    @Override
    public void addNew(ListingData listing) throws Exception.ListingAlreadyExists {

        try {
            jdbcTemplate.update(
                """
                        INSERT INTO listings (id, created_at, created_at_nano, updated_at, updated_at_nano, owner_id, title, description, status, photos)
                        VALUES (:id, :created_at, :created_at_nano, :updated_at, :updated_at_nano, :owner_id, :title, :description, :status, :photos::jsonb)
                    """,
                listingDataToParams(listing)
            );
        } catch (DuplicateKeyException e) {
            throw new Exception.ListingAlreadyExists();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    Map<String, Object> listingDataToParams(ListingData listingData) throws JsonProcessingException {
//        final String encodedPhoto;
//
//        if (listingData.photos().isPresent()) {
//            encodedPhoto = objectMapper.writeValueAsString(listingData.photo().get());
//        } else {
//            encodedPhoto = null;
//        }

        final var params = new HashMap<String, Object>();
        params.put("id", listingData.id());
        params.put("created_at", Timestamp.valueOf(listingData.createdAt().withNano(0)));
        params.put("created_at_nano", listingData.createdAt().getNano());
        params.put("updated_at", Timestamp.valueOf(listingData.updatedAt().withNano(0)));
        params.put("updated_at_nano", listingData.updatedAt().getNano());
        params.put("owner_id", listingData.ownerId());
        params.put("title", listingData.title());
        params.put("description", listingData.description().orElse(null));
        params.put("status", listingData.status());
        params.put("photos", null);

        return params;
    }

    @Override
    public Optional<ListingData> findById(UUID id) {

        try {
            final var foundListing = jdbcTemplate.queryForObject(
                """
                        SELECT * FROM listings WHERE id = :id LIMIT 1
                    """,
                Map.of("id", id),
                rowMapper
            );

            return Optional.ofNullable(foundListing);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @RequiredArgsConstructor
    private static final class Mapper implements RowMapper<ListingData> {

        private final ObjectMapper objectMapper;

        @Override
        public ListingData mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new ListingData(
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
