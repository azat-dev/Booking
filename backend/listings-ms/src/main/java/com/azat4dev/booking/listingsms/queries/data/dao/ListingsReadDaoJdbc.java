package com.azat4dev.booking.listingsms.queries.data.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public final class ListingsReadDaoJdbc implements ListingsReadDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<ListingRecord> findById(UUID listingId) {
        return Optional.empty();
    }
}
