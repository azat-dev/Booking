package com.azat4dev.demobooking.users.users_commands.domain.values;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

public interface UserIdFactory {

    UserId generateNewUserId();
}
