package com.azat4dev.booking.users.users_queries.domain.interfaces.repositories;

import com.azat4dev.booking.users.users_queries.data.dao.records.UserRecord;
import com.azat4dev.booking.users.users_queries.domain.entities.UserPhoto;

@FunctionalInterface
public interface MapToUserPhoto {
    UserPhoto map(UserRecord.PhotoPath photoPath);
}
