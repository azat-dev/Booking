package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.events.SentEmailForPasswordReset;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils.BuildResetPasswordEmail;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SendResetPasswordEmailImpl implements SendResetPasswordEmail {

    private final UsersRepository usersRepository;
    private final BuildResetPasswordEmail buildResetPasswordEmail;
    private final EmailService emailService;
    private final DomainEventsBus bus;

    @Override
    public void execute(IdempotentOperationId operationId, EmailAddress email) throws Exception {

        final var user = usersRepository.findByEmail(email)
            .orElseThrow(Exception.EmailNotFound::new);

        final var userId = user.getId();
        final var emailData = buildResetPasswordEmail.execute(userId, email);

        try {
            emailService.send(
                email,
                new EmailService.EmailData(
                    emailData.fromAddress(),
                    emailData.fromName(),
                    emailData.subject(),
                    emailData.body()
                )
            );

            bus.publish(new SentEmailForPasswordReset(userId, email));
        } catch (Throwable e) {
            bus.publish(
                new FailedToSendVerificationEmail(
                    userId,
                    email,
                    1
                )
            );
            throw new Exception.FailedToSendResetPasswordEmail();
        }
    }
}
