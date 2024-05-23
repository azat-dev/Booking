package com.azat4dev.booking.users.users_queries.domain.interfaces.repositories;

import com.azat4dev.booking.users.users_queries.data.dao.records.UserRecord;
import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;

@FunctionalInterface
public interface MapUserRecordToPersonalInfo {

    PersonalUserInfo map(UserRecord userRecord);
}
