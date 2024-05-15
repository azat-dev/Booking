package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToCompleteResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class CompletePasswordResetHandler implements CommandHandler<CompletePasswordReset> {

    private final ValidatePasswordResetTokenAnGetUserId validatePasswordResetTokenAnGetUserId;
    private final UsersRepository usersRepository;
    private final PasswordService passwordService;
    private final DomainEventsBus bus;

    @Override
    public void handle(CompletePasswordReset command, EventId eventId, LocalDateTime issuedAt) throws InvalidTokenException {

        try {
            final var userId = validatePasswordResetTokenAnGetUserId.execute(command.passwordResetToken());
            final var user = usersRepository.findById(userId).orElseThrow(InvalidTokenException::new);

            final var encodedPassword = passwordService.encodePassword(command.newPassword());
            user.setEncodedPassword(encodedPassword);

            usersRepository.update(user);
            bus.publish(new UserDidResetPassword(userId));

        } catch (ValidatePasswordResetTokenAnGetUserId.TokenExpiredException e) {
            bus.publish(new FailedToCompleteResetPassword(command.idempotentOperationToken()));
            throw new TokenExpiredException();
        } catch (ValidatePasswordResetTokenAnGetUserId.InvalidTokenException e) {
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
