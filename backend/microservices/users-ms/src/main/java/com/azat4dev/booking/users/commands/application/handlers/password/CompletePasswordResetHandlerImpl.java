package com.azat4dev.booking.users.commands.application.handlers.password;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.commands.application.commands.password.CompletePasswordReset;
import com.azat4dev.booking.users.commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SetNewPasswordByToken;
import com.azat4dev.booking.users.commands.domain.interfaces.services.PasswordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

            log.atInfo().log("Password reset completed");
        } catch (IdempotentOperationId.Exception e) {
            log.atWarn().setCause(e).log("Invalid operation id");
            throw ValidationException.withPath("operationId", e);
        } catch (TokenForPasswordReset.Exception e) {
            log.atWarn().log("Invalid token");
            throw ValidationException.withPath("passwordResetToken", e);
        } catch (Password.Exception e) {
            log.atWarn().log("Invalid password");
            throw ValidationException.withPath("password", e);
        } catch (SetNewPasswordByToken.Exception.InvalidToken e) {
            log.atWarn().log("Invalid token");
            throw new Exception.InvalidToken();
        } catch (SetNewPasswordByToken.Exception.TokenExpired e) {
            log.atWarn().setCause(e).log("Token is expired");
            throw new Exception.TokenExpired();
        }
    }
}
