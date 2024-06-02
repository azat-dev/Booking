package com.azat4dev.booking.listingsms.e2e.helpers;

import com.azat4dev.booking.shared.domain.values.user.UserId;

public interface GenerateAccessToken {
    String execute(UserId userId);
}