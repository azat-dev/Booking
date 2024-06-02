package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedToCompleteResetPassword;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class SetNewPasswordByTokenImpl implements SetNewPasswordByToken {

    private final ValidateTokenForPasswordResetAndGetUserId validateTokenAndGetUserId;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;

    @Override
    public void execute(
        IdempotentOperationId operationId,
        TokenForPasswordReset token,
        EncodedPassword encodedPassword
    ) throws Exception.InvalidToken, Exception.TokenExpired {

        final Runnable publishFailedEvent = () -> {
            bus.publish(new FailedToCompleteResetPassword(operationId));
        };

        try {
            final var userId = validateTokenAndGetUserId.execute(token);
            final var user = usersRepository.findById(userId).orElseThrow(Exception.InvalidToken::new);

            user.setEncodedPassword(encodedPassword);

            usersRepository.update(user);
            bus.publish(new UserDidResetPassword(userId));

        } catch (ValidateTokenForPasswordResetAndGetUserId.Exception.TokenExpired e) {

            publishFailedEvent.run();
            throw new Exception.TokenExpired();
        } catch (ValidateTokenForPasswordResetAndGetUserId.Exception.InvalidToken e) {

            publishFailedEvent.run();
            throw new Exception.InvalidToken();

        } catch (User.Exception.PasswordIsRequired e) {
            // Can't happen
            publishFailedEvent.run();
            throw new RuntimeException(e);
        }
    }
}
