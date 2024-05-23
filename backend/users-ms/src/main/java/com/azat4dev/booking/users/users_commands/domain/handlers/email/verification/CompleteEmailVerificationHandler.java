package com.azat4dev.booking.users.users_commands.domain.handlers.email.verification;


import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.shared.domain.event.EventId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils.GetInfoForEmailVerificationToken;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class CompleteEmailVerificationHandler implements CommandHandler<CompleteEmailVerification> {

    private final GetInfoForEmailVerificationToken getTokenInfo;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;
    private final TimeProvider timeProvider;

    @Override
    public void handle(CompleteEmailVerification command, EventId eventId, LocalDateTime issuedAt) throws Exception {

        final var token = command.token();

        try {
            final var tokenInfo = getTokenInfo.execute(token);

            final var userId = tokenInfo.userId();
            final var email = tokenInfo.email();
            final var now = timeProvider.currentTime();

            if (tokenInfo.expiresAt().isBefore(now)) {
                throw new Exception.TokenIsExpired();
            }

            final var userResult = usersRepository.findById(userId);
            final var user = userResult.orElseThrow(Exception.TokenIsNotValid::new);

            if (!user.getEmail().equals(email)) {
                throw new Exception.TokenIsNotValid();
            }

            user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
            usersRepository.update(user);

            bus.publish(new UserVerifiedEmail(userId, email));

        } catch (GetInfoForEmailVerificationToken.TokenIsNotValidException e) {
            throw new Exception.TokenIsNotValid();
        } catch (
            User.Exception.EmailVerificationStatusIsRequired e) {
            throw new RuntimeException(e);
        }
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class TokenIsNotValid extends Exception {
            public TokenIsNotValid() {
                super("Token is not valid");
            }
        }

        public static final class TokenIsExpired extends Exception {
            public TokenIsExpired() {
                super("Token is expired");
            }
        }
    }
}
