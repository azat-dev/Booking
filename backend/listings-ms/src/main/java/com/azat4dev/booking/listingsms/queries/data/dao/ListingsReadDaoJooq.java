package com.azat4dev.booking.listingsms.queries.data.dao;

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
    public List<ListingsRecord> findAllByOwnerId(UUID ownerId) {
        return context.selectFrom(LISTINGS)
            .where(LISTINGS.OWNER_ID.eq(ownerId))
            .fetchInto(ListingsRecord.class);
    }
}
