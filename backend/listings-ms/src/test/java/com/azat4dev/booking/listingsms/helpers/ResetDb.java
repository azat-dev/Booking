package com.azat4dev.booking.listingsms.helpers;

import org.springframework.test.context.jdbc.Sql;

@Sql("/db/drop-schema.sql")
@Sql("/db/schema.sql")
public @interface ResetDb {
}
