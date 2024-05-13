package com.azat4dev.demobooking.common.domain.core;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

public interface UserIdFactory {

    UserId generateNewUserId();
}
