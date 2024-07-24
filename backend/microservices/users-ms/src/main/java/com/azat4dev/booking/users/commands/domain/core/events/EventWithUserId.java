package com.azat4dev.booking.users.commands.domain.core.events;

import com.azat4dev.booking.shared.domain.values.user.UserId;

public interface EventWithUserId {
    UserId userId();
}
