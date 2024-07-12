package com.azat4dev.booking.users.commands.domain.handlers.password.reset;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.commands.domain.core.entities.User;
import com.azat4dev.booking.users.commands.domain.core.events.FailedToCompleteResetPassword;
import com.azat4dev.booking.users.commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.booking.users.commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Observed
@Slf4j
@RequiredArgsConstructor
public class SetNewPasswordByTokenImpl implements SetNewPasswordByToken {

    private final ValidateTokenForPasswordResetAndGetUserId validateTokenAndGetUserId;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;

    @Override
    public void execute(
            IdempotentOperationId operationId,
            TokenForPasswordReset token,
            EncodedPassword encodedPassword
    ) throws Exception.InvalidToken, Exception.TokenExpired {

        final Runnable publishFailedEvent = () -> bus.publish(new FailedToCompleteResetPassword(operationId));

        try {
            final var userId = validateTokenAndGetUserId.execute(token);
            final var user = usersRepository.findById(userId).orElseThrow(Exception.InvalidToken::new);

            user.setEncodedPassword(encodedPassword);

            usersRepository.update(user);
            log.atInfo()
                    .addKeyValue("userId", userId)
                    .log("User did reset password");

            bus.publish(new UserDidResetPassword(userId));
            log.atDebug().log("Success event published");

        } catch (ValidateTokenForPasswordResetAndGetUserId.Exception.TokenExpired e) {

            log.atDebug()
                    .setCause(e)
                    .log("Token is expired");

            publishFailedEvent.run();
            throw new Exception.TokenExpired();
        } catch (ValidateTokenForPasswordResetAndGetUserId.Exception.InvalidToken e) {

            log.atDebug()
                    .setCause(e)
                    .log("Token is invalid");
            publishFailedEvent.run();
            throw new Exception.InvalidToken();

        } catch (User.Exception.PasswordIsRequired e) {

            log.atDebug()
                    .setCause(e)
                    .log("Password is required");

            publishFailedEvent.run();
            throw new RuntimeException(e);
        }
    }
}
