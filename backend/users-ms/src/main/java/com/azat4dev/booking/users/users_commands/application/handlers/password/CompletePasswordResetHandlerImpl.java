package com.azat4dev.booking.users.users_commands.application.handlers.password;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.application.commands.password.CompletePasswordReset;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.SetNewPasswordByToken;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.PasswordService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class CompletePasswordResetHandlerImpl implements CompletePasswordResetHandler {

    private final SetNewPasswordByToken setNewPasswordByToken;
    private final PasswordService passwordService;

    @Override
    public void handle(CompletePasswordReset command) throws Exception {

        try {
            final var operationId = IdempotentOperationId.checkAndMakeFrom(command.operationId());
            final var token = TokenForPasswordReset.checkAndMakeFrom(command.passwordResetToken());
            final var newPassword = Password.checkAndMakeFromString(command.newPassword());
            final var newEncodedPassword = passwordService.encodePassword(newPassword);

            setNewPasswordByToken.execute(
                operationId,
                token,
                newEncodedPassword
            );
        } catch (IdempotentOperationId.Exception e) {
            throw ValidationException.withPath("operationId", e);
        } catch (TokenForPasswordReset.Exception e) {
            throw ValidationException.withPath("passwordResetToken", e);
        } catch (Password.Exception e) {
            throw ValidationException.withPath("password", e);
        } catch (SetNewPasswordByToken.Exception.InvalidToken e) {
            throw new Exception.InvalidToken();
        } catch (SetNewPasswordByToken.Exception.TokenExpired e) {
            throw new Exception.TokenExpired();
        }
    }
}
