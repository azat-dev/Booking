package com.azat4dev.booking.users.commands.domain.handlers.email.verification;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils.GetInfoForEmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class VerifyEmailByTokenImpl implements VerifyEmailByToken {

    private final GetInfoForEmailVerificationToken getTokenInfo;
    private final Users users;
    private final TimeProvider timeProvider;

    @Override
    public void execute(EmailVerificationToken token)
        throws Exception.TokenIsExpired, Exception.TokenIsNotValid,
        Exception.EmailNotFound, Exception.UserNotFound {

        try {
            final var tokenInfo = getTokenInfo.execute(token);

            final var userId = tokenInfo.userId();
            final var email = tokenInfo.email();
            final var now = timeProvider.currentTime();

            if (tokenInfo.expiresAt().isBefore(now)) {
                throw new Exception.TokenIsExpired();
            }

            users.addVerifiedEmail(
                userId,
                email
            );

        } catch (GetInfoForEmailVerificationToken.TokenIsNotValidException e) {
            throw new Exception.TokenIsNotValid();
        } catch (Users.Exception.UserNotFound e) {
            throw new Exception.UserNotFound();
        } catch (Users.Exception.EmailNotFound e) {
            throw new Exception.EmailNotFound();
        }
    }
}