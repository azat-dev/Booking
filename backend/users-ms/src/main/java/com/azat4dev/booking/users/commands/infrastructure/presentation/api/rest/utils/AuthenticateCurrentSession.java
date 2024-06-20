package com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;

public interface AuthenticateCurrentSession {

    Tokens execute(UserId userId, String[] roles);

    record Tokens(String accessToken, String refreshToken) { }
}
