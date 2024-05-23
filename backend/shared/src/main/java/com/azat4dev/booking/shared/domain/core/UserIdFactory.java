package com.azat4dev.booking.shared.domain.core;

import com.azat4dev.booking.shared.domain.core.UserId;

public interface UserIdFactory {

    UserId generateNewUserId();
}
