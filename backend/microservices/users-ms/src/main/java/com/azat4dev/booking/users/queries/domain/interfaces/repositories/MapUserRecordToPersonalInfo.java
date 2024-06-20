package com.azat4dev.booking.users.queries.domain.interfaces.repositories;

import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.records.UserRecord;
import com.azat4dev.booking.users.queries.domain.entities.PersonalUserInfo;

@FunctionalInterface
public interface MapUserRecordToPersonalInfo {

    PersonalUserInfo map(UserRecord userRecord);
}
