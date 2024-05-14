package com.azat4dev.demobooking.users.users_commands.domain.handlers;


import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.GetInfoForEmailVerificationToken;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CompleteEmailVerificationHandler implements CommandHandler<DomainEventNew<CompleteEmailVerification>> {

    private final GetInfoForEmailVerificationToken getTokenInfo;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;
    private final DomainEventsFactory domainEventsFactory;
    private final TimeProvider timeProvider;

    @Override
    public void handle(DomainEventNew<CompleteEmailVerification> command) throws TokenIsExpiredException, TokenIsNotValidException {

        final var payload = command.payload();
        final var token = payload.token();

        try {
            final var tokenInfo = getTokenInfo.execute(token);

            final var userId = tokenInfo.userId();
            final var email = tokenInfo.email();
            final var now = timeProvider.currentTime();

            if (tokenInfo.expiresAt().isBefore(now)) {
                throw new TokenIsExpiredException();
            }

            final var userResult = usersRepository.findById(userId);
            final var user = userResult.orElseThrow(TokenIsNotValidException::new);

            if (!user.getEmail().equals(email)) {
                throw new TokenIsNotValidException();
            }

            user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
            usersRepository.save(user);

            bus.publish(
                domainEventsFactory.issue(
                    new UserVerifiedEmail(userId, email)
                )
            );

        } catch (GetInfoForEmailVerificationToken.TokenIsNotValidException e) {
            throw new TokenIsNotValidException();
        }
    }

    // Exceptions

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static final class TokenIsNotValidException extends ValidationException {
        public TokenIsNotValidException() {
            super("Token is not valid");
        }

        @Override
        public String getCode() {
            return "TokenIsNotValid";
        }
    }

    public static final class TokenIsExpiredException extends ValidationException {
        public TokenIsExpiredException() {
            super("Token is expired");
        }

        @Override
        public String getCode() {
            return "TokenIsExpired";
        }
    }
}
