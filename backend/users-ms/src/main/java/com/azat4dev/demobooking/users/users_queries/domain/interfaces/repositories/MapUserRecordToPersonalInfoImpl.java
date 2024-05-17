package com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.LastName;
import com.azat4dev.demobooking.users.users_queries.data.dao.records.UserRecord;
import com.azat4dev.demobooking.users.users_queries.domain.entities.PersonalUserInfo;

public class MapUserRecordToPersonalInfoImpl implements MapUserRecordToPersonalInfo {

    @Override
    public PersonalUserInfo map(UserRecord userRecord) {
        try {
            return new PersonalUserInfo(
                UserId.fromUUID(userRecord.id()),
                userRecord.email(),
                new FullName(
                    FirstName.dangerMakeFromStringWithoutCheck(userRecord.firstName()),
                    LastName.dangerMakeFromStringWithoutCheck(userRecord.lastName())
                )
            );
        } catch (UserId.WrongFormatException | FullName.Exception e) {
            throw new RuntimeException(e);
        }
    }
}
