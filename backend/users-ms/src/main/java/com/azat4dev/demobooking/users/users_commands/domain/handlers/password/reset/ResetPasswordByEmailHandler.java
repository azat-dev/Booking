package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset;

import com.azat4dev.demobooking.common.domain.CommandHandler;
import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.ResetPasswordByEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.SentEmailForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils.BuildResetPasswordEmail;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class ResetPasswordByEmailHandler implements CommandHandler<ResetPasswordByEmail> {

    private final UsersRepository usersRepository;
    private final BuildResetPasswordEmail buildResetPasswordEmail;
    private final EmailService emailService;
    private final DomainEventsBus bus;

    @Override
    public void handle(ResetPasswordByEmail command, EventId eventId, LocalDateTime issuedAt) throws Exception {

        final var user = usersRepository.findByEmail(command.email())
            .orElseThrow(Exception.EmailNotFound::new);

        final var userId = user.getId();

        final var emailData = buildResetPasswordEmail.execute(
            userId,
            command.email()
        );

        try {
            emailService.send(
                command.email(),
                new EmailService.EmailData(
                    emailData.fromAddress(),
                    emailData.fromName(),
                    emailData.subject(),
                    emailData.body()
                )
            );
        } catch (Throwable e) {
            bus.publish(
                new FailedToSendVerificationEmail(
                    userId,
                    command.email(),
                    1
                )
            );
            throw new Exception.FailedToSendResetPasswordEmail();
        }

        bus.publish(
            new SentEmailForPasswordReset(
                userId,
                command.email()
            )
        );
    }

    // Exceptions

    public static abstract class Exception extends DomainException {

        public Exception(String message) {
            super(message);
        }

        public static final class EmailNotFound extends Exception {
            public EmailNotFound() {
                super("Email not found");
            }
        }

        public static final class FailedToSendResetPasswordEmail extends Exception {
            public FailedToSendResetPasswordEmail() {
                super("Failed to send reset password email");
            }
        }
    }
}
