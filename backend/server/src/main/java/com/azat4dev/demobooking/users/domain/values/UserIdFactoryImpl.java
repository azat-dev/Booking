package com.azat4dev.demobooking.users.domain.values;

public class UserIdFactoryImpl implements UserIdFactory {

    @Override
    public UserId generateNewUserId() {
        return UserId.generateNew();
    }
}
