package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.utils;

import com.azat4dev.booking.shared.domain.core.UserId;

public interface AuthenticateCurrentSession {

    Tokens execute(UserId userId, String[] roles);

    public static record Tokens(String accessToken, String refreshToken) { }
}
