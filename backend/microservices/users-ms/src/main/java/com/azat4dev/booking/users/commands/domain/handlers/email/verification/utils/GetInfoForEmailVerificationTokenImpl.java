package com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RequiredArgsConstructor
public final class GetInfoForEmailVerificationTokenImpl implements GetInfoForEmailVerificationToken {

    private final JwtDecoder jwtDecoder;

    @Override
    public EmailVerificationTokenInfo execute(EmailVerificationToken token) throws TokenIsNotValidException {

        try {
            final var decoded = jwtDecoder.decode(token.value());
            final var subject = decoded.getSubject();

            final var info = new EmailVerificationTokenInfo(
                UserId.checkAndMakeFrom(subject),
                EmailAddress.checkAndMakeFromString(decoded.getClaim("email")),
                LocalDateTime.ofInstant(decoded.getExpiresAt(), ZoneOffset.UTC)
            );

            log.debug("Got info for email verification token");
            return info;
        } catch (JwtException | UserId.WrongFormatException | EmailAddress.WrongFormatException e) {
            log.error("Token is not valid", e);
            throw new TokenIsNotValidException();
        }
    }
}
