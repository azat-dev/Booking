package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToCompleteResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class CompletePasswordResetHandler implements CommandHandler<CompletePasswordReset> {

    private final ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;

    @Override
    public void handle(CompletePasswordReset command, EventId eventId, LocalDateTime issuedAt) throws InvalidTokenException {

        try {
            final var userId = validateTokenForPasswordResetAndGetUserId.execute(command.passwordResetToken());
            final var user = usersRepository.findById(userId).orElseThrow(InvalidTokenException::new);

            user.setEncodedPassword(command.newPassword());

            usersRepository.update(user);
            bus.publish(new UserDidResetPassword(userId));

        } catch (ValidateTokenForPasswordResetAndGetUserId.TokenExpiredException e) {
            bus.publish(new FailedToCompleteResetPassword(command.idempotentOperationToken()));
            throw new TokenExpiredException();
        } catch (ValidateTokenForPasswordResetAndGetUserId.InvalidTokenException e) {
            bus.publish(new FailedToCompleteResetPassword(command.idempotentOperationToken()));
            throw new InvalidTokenException();
        }
    }

    // Exceptions

    public static final class InvalidTokenException extends DomainException {
        public InvalidTokenException() {
            super("Invalid token");
        }

        @Override
        public String getCode() {
            return "InvalidToken";
        }
    }

    public static final class TokenExpiredException extends DomainException {
        public TokenExpiredException() {
            super("Token is expired");
        }

        @Override
        public String getCode() {
            return "TokenExpired";
        }
    }
}
