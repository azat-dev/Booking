package com.azat4dev.demobooking.users.users_commands.domain.values;

public class UserIdFactoryImpl implements UserIdFactory {

    @Override
    public UserId generateNewUserId() {
        return UserId.generateNew();
    }
}
