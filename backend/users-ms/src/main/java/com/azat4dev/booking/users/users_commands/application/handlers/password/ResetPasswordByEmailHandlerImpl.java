package com.azat4dev.booking.users.users_commands.application.handlers.password;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.users.users_commands.application.commands.password.ResetPasswordByEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.SendResetPasswordEmail;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ResetPasswordByEmailHandlerImpl implements ResetPasswordByEmailHandler {

    private final SendResetPasswordEmail sendResetPasswordEmail;

    @Override
    public void handle(ResetPasswordByEmail command) throws Exception, ValidationException {

        try {
            final var operationId = IdempotentOperationId.checkAndMakeFrom(command.getOperationId());
            final var email = EmailAddress.checkAndMakeFromString(command.getEmail());

            sendResetPasswordEmail.execute(operationId, email);

        } catch (IdempotentOperationId.Exception e) {
            throw ValidationException.withPath("operationId", e);
        } catch (EmailAddress.WrongFormatException e) {
            throw ValidationException.withPath("email", e);
        } catch (SendResetPasswordEmail.Exception e) {
            throw new Exception.FailedToSendResetPasswordEmail();
        }
    }
}
