package com.azat4dev.booking.users.commands.domain.handlers.email.verification;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils.GetInfoForEmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

            log.atInfo().log("Email verified");

        } catch (GetInfoForEmailVerificationToken.TokenIsNotValidException e) {
            log.atInfo().setCause(e).log("Token is not valid");
            throw new Exception.TokenIsNotValid();
        } catch (Users.Exception.UserNotFound e) {
            log.atInfo().setCause(e).log("User not found");
            throw new Exception.UserNotFound();
        } catch (Users.Exception.EmailNotFound e) {
            log.atInfo().setCause(e).log("Email not found");
            throw new Exception.EmailNotFound();
        }
    }
}