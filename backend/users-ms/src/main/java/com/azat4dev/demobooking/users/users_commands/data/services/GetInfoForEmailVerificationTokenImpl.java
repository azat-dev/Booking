package com.azat4dev.demobooking.users.users_commands.data.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationTokenInfo;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.GetInfoForEmailVerificationToken;
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
                UserId.fromString(subject),
                EmailAddress.checkAndMakeFromString(decoded.getClaim("email")),
                LocalDateTime.ofInstant(decoded.getExpiresAt(), ZoneOffset.UTC)
            );
        } catch (JwtException e) {
            throw new TokenIsNotValidException();
        }
    }
}
