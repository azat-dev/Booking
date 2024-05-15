package com.azat4dev.demobooking.users.users_commands.data.services.password;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.PasswordResetToken;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.ValidateTokenForPasswordResetAndGetUserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ValidateTokenForPasswordResetAndGetUserIdImpl implements ValidateTokenForPasswordResetAndGetUserId {

    private final GetInfoForPasswordResetToken getInfoForPasswordResetToken;
    private final TimeProvider currentTimeProvider;

    @Override
    public UserId execute(PasswordResetToken token) throws InvalidTokenException {

        return null;
    }
}
