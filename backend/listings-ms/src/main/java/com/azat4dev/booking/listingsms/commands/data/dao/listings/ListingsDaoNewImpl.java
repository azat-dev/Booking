package com.azat4dev.booking.listingsms.commands.data.dao.listings;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.generated.Tables.LISTINGS;


@AllArgsConstructor
public class ListingsDaoNewImpl implements ListingsDaoNew {

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
}
