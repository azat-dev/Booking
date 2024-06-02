/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated;


import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.generated.tables.Listings;
import org.jooq.generated.tables.OutboxEvents;
import org.jooq.generated.tables.records.ListingsRecord;
import org.jooq.generated.tables.records.OutboxEventsRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ListingsRecord> LISTINGS_PKEY = Internal.createUniqueKey(Listings.LISTINGS, DSL.name("listings_pkey"), new TableField[] { Listings.LISTINGS.ID }, true);
    public static final UniqueKey<OutboxEventsRecord> OUTBOX_EVENTS_PKEY = Internal.createUniqueKey(OutboxEvents.OUTBOX_EVENTS, DSL.name("outbox_events_pkey"), new TableField[] { OutboxEvents.OUTBOX_EVENTS.EVENT_ID, OutboxEvents.OUTBOX_EVENTS.EVENT_TYPE }, true);
}
