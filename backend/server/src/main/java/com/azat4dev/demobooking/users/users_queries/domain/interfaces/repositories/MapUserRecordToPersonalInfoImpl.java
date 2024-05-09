package com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.LastName;
import com.azat4dev.demobooking.users.users_queries.data.dao.records.UserRecord;
import com.azat4dev.demobooking.users.users_queries.domain.entities.PersonalUserInfo;

public class MapUserRecordToPersonalInfoImpl implements MapUserRecordToPersonalInfo {

    @Override
    public PersonalUserInfo map(UserRecord userRecord) {
        return new PersonalUserInfo(
            UserId.fromUUID(userRecord.id()),
            userRecord.email(),
            new FullName(
                FirstName.dangerMakeFromStringWithoutCheck(userRecord.firstName()),
                LastName.dangerMakeFromStringWithoutCheck(userRecord.lastName()
                )
            )
        );
    }
}
