package com.azat4dev.booking.users.users_commands.application.handlers;

import com.azat4dev.booking.users.users_commands.application.commands.email.verification.CompleteEmailVerification;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByToken;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class CompleteEmailVerificationHandlerImpl implements CompleteEmailVerificationHandler {

    private final VerifyEmailByToken verifyEmailByToken;

    @Override
    public void handle(CompleteEmailVerification command) throws Exception {

        final var token = new EmailVerificationToken(command.token());

        try {
            verifyEmailByToken.execute(token);

        } catch (VerifyEmailByToken.Exception.TokenIsExpired e) {
            throw new Exception.TokenIsExpired();
        } catch (VerifyEmailByToken.Exception.TokenIsNotValid e) {
            throw new Exception.TokenIsNotValid();
        } catch (VerifyEmailByToken.Exception.EmailNotFound e) {
            throw new Exception.EmailNotFound();
        } catch (VerifyEmailByToken.Exception.UserNotFound e) {
            throw new Exception.UserNotFound();
        }
    }
}
