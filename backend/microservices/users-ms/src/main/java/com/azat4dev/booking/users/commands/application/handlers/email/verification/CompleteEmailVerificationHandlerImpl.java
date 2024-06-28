package com.azat4dev.booking.users.commands.application.handlers.email.verification;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.users.commands.application.commands.email.verification.CompleteEmailVerification;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.VerifyEmailByToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public final class CompleteEmailVerificationHandlerImpl implements CompleteEmailVerificationHandler {

    private final VerifyEmailByToken verifyEmailByToken;

    @Override
    public void handle(CompleteEmailVerification command) throws Exception, ValidationException {

        final var token = new EmailVerificationToken(command.token());

        try {
            verifyEmailByToken.execute(token);
            log.info("Email verification completed");
        } catch (VerifyEmailByToken.Exception.TokenIsExpired e) {
            log.debug("Token is expired", e);
            throw new Exception.TokenIsExpired();
        } catch (VerifyEmailByToken.Exception.TokenIsNotValid e) {
            log.debug("Token is not valid", e);
            throw new Exception.TokenIsNotValid();
        } catch (VerifyEmailByToken.Exception.EmailNotFound e) {
            log.debug("Email not found", e);
            throw new Exception.EmailNotFound();
        } catch (VerifyEmailByToken.Exception.UserNotFound e) {
            log.debug("User not found", e);
            throw new Exception.UserNotFound();
        }
    }
}
