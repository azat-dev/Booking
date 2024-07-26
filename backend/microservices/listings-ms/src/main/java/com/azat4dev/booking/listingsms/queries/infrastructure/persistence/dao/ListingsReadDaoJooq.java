package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.dao;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.generated.Tables.LISTINGS;

@AllArgsConstructor
public final class ListingsReadDaoJooq implements ListingsReadDao {

    private final DSLContext context;

    @Override
    public Optional<ListingsRecord> findById(UUID listingId) {

        return context.selectFrom(LISTINGS)
            .where(LISTINGS.ID.eq(listingId))
            .fetchOptional();
    }

    @Override
    public List<ListingsRecord> findAllByHostId(UUID hostId) {
        return context.selectFrom(LISTINGS)
            .where(LISTINGS.HOST_ID.eq(hostId))
            .fetchInto(ListingsRecord.class);
    }
}
