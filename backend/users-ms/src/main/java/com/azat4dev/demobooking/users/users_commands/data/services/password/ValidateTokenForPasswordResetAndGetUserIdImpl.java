package com.azat4dev.demobooking.users.users_commands.data.services.password;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.TokenForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.ValidateTokenForPasswordResetAndGetUserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ValidateTokenForPasswordResetAndGetUserIdImpl implements ValidateTokenForPasswordResetAndGetUserId {

    private final GetInfoForPasswordResetToken getTokenInfo;
    private final TimeProvider timeProvider;

    @Override
    public UserId execute(TokenForPasswordReset token) throws InvalidTokenException {

        try {
            final var parsedData = getTokenInfo.execute(token);
            final var now = timeProvider.currentTime();

            if (now.isAfter(parsedData.expiresAt())) {
                throw new ValidateTokenForPasswordResetAndGetUserId.TokenExpiredException();
            }

            return parsedData.userId();
            
        } catch (GetInfoForPasswordResetToken.InvalidTokenException e) {
            throw new ValidateTokenForPasswordResetAndGetUserId.InvalidTokenException();
        }
    }
}
