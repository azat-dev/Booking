package com.azat4dev.booking.users.queries.domain.interfaces.repositories;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.UsersReadDao;
import com.azat4dev.booking.users.queries.domain.entities.PersonalUserInfo;
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
