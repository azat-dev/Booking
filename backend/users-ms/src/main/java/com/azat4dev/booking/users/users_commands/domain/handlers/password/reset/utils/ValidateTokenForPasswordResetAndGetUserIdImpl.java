package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ValidateTokenForPasswordResetAndGetUserIdImpl implements ValidateTokenForPasswordResetAndGetUserId {

    private final GetInfoForPasswordResetToken getTokenInfo;
    private final TimeProvider timeProvider;

    @Override
    public UserId execute(TokenForPasswordReset token) throws Exception {

        try {
            final var parsedData = getTokenInfo.execute(token);
            final var now = timeProvider.currentTime();

            if (now.isAfter(parsedData.expiresAt())) {
                throw new ValidateTokenForPasswordResetAndGetUserId.Exception.TokenExpired();
            }

            return parsedData.userId();

        } catch (GetInfoForPasswordResetToken.Exception.InvalidToken e) {
            throw new ValidateTokenForPasswordResetAndGetUserId.Exception.InvalidToken();
        }
    }
}
