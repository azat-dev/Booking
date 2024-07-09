package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class ValidateTokenForPasswordResetAndGetUserIdImpl implements ValidateTokenForPasswordResetAndGetUserId {

    private final GetInfoForPasswordResetToken getTokenInfo;
    private final TimeProvider timeProvider;

    @Override
    public UserId execute(TokenForPasswordReset token) throws Exception.InvalidToken, Exception.TokenExpired {

        try {
            final var parsedData = getTokenInfo.execute(token);
            final var now = timeProvider.currentTime();

            if (now.isAfter(parsedData.expiresAt())) {
                log.atDebug().log("Token is expired");
                throw new Exception.TokenExpired();
            }

            final var userId = parsedData.userId();
            log.atDebug().log("Password reset token is valid");
            return userId;

        } catch (GetInfoForPasswordResetToken.Exception.InvalidToken e) {
            log.atDebug().log("Token is not valid");
            throw new Exception.InvalidToken();
        }
    }
}
