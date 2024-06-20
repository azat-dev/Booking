package com.azat4dev.booking.listingsms.commands.infrastructure.dao.listings;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.generated.Tables.LISTINGS;


@AllArgsConstructor
public final class ListingsDaoImpl implements ListingsDao {

    private final DSLContext context;

    @Override
    public void addNew(ListingsRecord listing) throws Exception.ListingAlreadyExists {

        context.newRecord(LISTINGS, listing)
            .store();
    }

    @Override
    public Optional<ListingsRecord> findById(UUID id) {
        return context.selectFrom(LISTINGS)
            .where(LISTINGS.ID.eq(id))
            .fetchOptional();
    }

    @Override
    public void update(ListingsRecord listing) throws Exception.ListingNotFound {

        final var numberOfUpdatedRecords = context.update(LISTINGS)
            .set(listing)
            .where(LISTINGS.ID.eq(listing.getId()))
            .execute();

        if (numberOfUpdatedRecords == 0) {
            throw new Exception.ListingNotFound();
        }
    }
}
