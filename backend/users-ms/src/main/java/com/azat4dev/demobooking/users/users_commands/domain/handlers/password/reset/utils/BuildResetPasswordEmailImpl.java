package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BuildResetPasswordEmailImpl implements BuildResetPasswordEmail {

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
                    + link
                    + "\n\n"
                    + "If you did not request to reset your password, please ignore this email."
                    + "\n\n"
                    + "Thank you."
            )
        );
    }
}
