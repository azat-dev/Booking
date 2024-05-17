package com.azat4dev.demobooking.users.users_commands.domain.handlers.email.verification.utils;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.verification.EmailVerificationTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
public final class GetInfoForEmailVerificationTokenImpl implements GetInfoForEmailVerificationToken {

    private final JwtDecoder jwtDecoder;

    @Override
    public EmailVerificationTokenInfo execute(EmailVerificationToken token) throws TokenIsNotValidException {

        try {
            final var decoded = jwtDecoder.decode(token.value());
            final var subject = decoded.getSubject();

            return new EmailVerificationTokenInfo(
                UserId.checkAndMakeFrom(subject),
                EmailAddress.checkAndMakeFromString(decoded.getClaim("email")),
                LocalDateTime.ofInstant(decoded.getExpiresAt(), ZoneOffset.UTC)
            );
        } catch (JwtException | UserId.WrongFormatException | EmailAddress.WrongFormatException e) {
            throw new TokenIsNotValidException();
        }
    }
}
