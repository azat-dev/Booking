package com.azat4dev.booking.users.queries.domain.interfaces.repositories;

import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.records.UserRecord;
import com.azat4dev.booking.users.queries.domain.entities.UserPhoto;

@FunctionalInterface
public interface MapToUserPhoto {
    UserPhoto map(UserRecord.PhotoPath photoPath);
}
