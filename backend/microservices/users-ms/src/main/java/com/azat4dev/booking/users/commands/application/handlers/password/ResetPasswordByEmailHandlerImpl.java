package com.azat4dev.booking.users.commands.application.handlers.password;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.commands.application.commands.password.ResetPasswordByEmail;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SendResetPasswordEmail;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Observed
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordByEmailHandlerImpl implements ResetPasswordByEmailHandler {

    private final SendResetPasswordEmail sendResetPasswordEmail;

    @Override
    public void handle(ResetPasswordByEmail command) throws Exception.FailedToSendResetPasswordEmail, ValidationException {

        try {
            final var operationId = IdempotentOperationId.checkAndMakeFrom(command.getOperationId());
            final var email = EmailAddress.checkAndMakeFromString(command.getEmail());

            sendResetPasswordEmail.execute(operationId, email);
            log.atInfo().log("Reset password email sent");

        } catch (IdempotentOperationId.Exception e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("Invalid operation id");
            throw ValidationException.withPath("operationId", e);
        } catch (EmailAddress.WrongFormatException e) {
            log.atWarn().addKeyValue("code", e.getCode()).log("Invalid email");
            throw ValidationException.withPath("email", e);
        } catch (SendResetPasswordEmail.Exception e) {
            log.atError().setCause(e).log("Failed to send reset password email");
            throw new Exception.FailedToSendResetPasswordEmail();
        }
    }
}
