package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToCompleteResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class CompletePasswordResetHandler implements CommandHandler<CompletePasswordReset> {

    private final ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;

    @Override
    public void handle(CompletePasswordReset command, EventId eventId, LocalDateTime issuedAt) throws Exception {

        final Runnable publishFailedEvent = () -> {
            bus.publish(new FailedToCompleteResetPassword(command.idempotentOperationId()));
        };

        try {
            final var userId = validateTokenForPasswordResetAndGetUserId.execute(command.passwordResetToken());
            final var user = usersRepository.findById(userId).orElseThrow(Exception.InvalidToken::new);

            user.setEncodedPassword(command.newPassword());

            usersRepository.update(user);
            bus.publish(new UserDidResetPassword(userId));

        } catch (ValidateTokenForPasswordResetAndGetUserId.Exception e) {

            publishFailedEvent.run();
            throw Exception.makeFrom(e);

        } catch (User.Exception.PasswordIsRequired e) {
            // Can't happen
            publishFailedEvent.run();
            throw new RuntimeException(e);
        }
    }

    // Exceptions

    public static sealed abstract class Exception extends DomainException permits Exception.InvalidToken, Exception.TokenExpired {
        Exception(String message) {
            super(message);
        }

        public static <Input extends ValidateTokenForPasswordResetAndGetUserId.Exception> Exception makeFrom(Input e) {
            switch (e) {
                case Input.TokenExpired inst -> {
                    return new TokenExpired();
                }
                case Input.InvalidToken inst -> {
                    return new InvalidToken();
                }
            }
        }

        public static final class InvalidToken extends Exception {
            public InvalidToken() {
                super("Invalid token");
            }
        }

        public static final class TokenExpired extends Exception {
            public TokenExpired() {
                super("Token is expired");
            }
        }
    }
}
