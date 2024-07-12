package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailBody;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;

@Observed
@RequiredArgsConstructor
public class BuildResetPasswordEmailImpl implements BuildResetPasswordEmail {

    private final String fromName;
    private final EmailAddress fromAddress;
    private final String subject;
    private final GenerateResetPasswordLink generateResetPasswordLink;

    @Override
    public EmailData execute(UserId userId, EmailAddress email) {

        final var link = generateResetPasswordLink.execute(userId);
        return new EmailData(
            fromName,
            fromAddress,
            subject,
            new EmailBody(
                "Hello"
                    + "\n\n"
                    + "You have requested to reset your password."
                    + "\n\n"
                    + "Please click the link below to reset your password."
                    + "\n\n"
                    + "<a href=\"" + link + "\">Reset Password</a>"
                    + "\n\n"
                    + "If you did not request to reset your password, please ignore this email."
                    + "\n\n"
                    + "Thank you."
            )
        );
    }
}
