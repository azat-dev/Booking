package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.JwtDataDecoder;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetInfoForPasswordResetTokenImpl implements GetInfoForPasswordResetToken {

    private final JwtDataDecoder decoder;

    @Override
    public Data execute(TokenForPasswordReset token) throws Exception.InvalidToken {
        final var decodedData = decoder.decode(token.getValue());

        try {
            return new Data(
                UserId.checkAndMakeFrom(decodedData.subject()),
                decodedData.expiresAt()
            );
        } catch (UserId.WrongFormatException e) {
            throw new Exception.InvalidToken();
        }
    }
}
