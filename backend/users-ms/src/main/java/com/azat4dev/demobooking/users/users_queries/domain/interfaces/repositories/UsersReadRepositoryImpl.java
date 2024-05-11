package com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_queries.data.dao.UsersReadDao;
import com.azat4dev.demobooking.users.users_queries.domain.entities.PersonalUserInfo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UsersReadRepositoryImpl implements UsersReadRepository {
    private final UsersReadDao usersReadDao;
    private final MapUserRecordToPersonalInfo mapUserRecordToPersonalInfo;

    @Override
    public Optional<PersonalUserInfo> getPersonalUserInfoById(UserId id) {
        return usersReadDao.getById(id.value())
            .map(mapUserRecordToPersonalInfo::map);
    }
}
