package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataDecoder;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Observed
@Slf4j
@RequiredArgsConstructor
public class GetInfoForPasswordResetTokenImpl implements GetInfoForPasswordResetToken {

    private final JwtDataDecoder decoder;

    @Override
    public Data execute(TokenForPasswordReset token) throws Exception.InvalidToken {
        final var decodedData = decoder.decode(token.getValue());

        try {
            final var data = new Data(
                UserId.checkAndMakeFrom(decodedData.subject()),
                decodedData.expiresAt()
            );

            log.atTrace().log("Password reset token info extracted");
            return data;
        } catch (UserId.WrongFormatException e) {
            log.atWarn().log("Token is not valid");
            throw new Exception.InvalidToken();
        }
    }
}
