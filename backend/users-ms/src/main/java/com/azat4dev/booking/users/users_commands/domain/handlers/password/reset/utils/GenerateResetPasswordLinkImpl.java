package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.core.UserId;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public final class GenerateResetPasswordLinkImpl implements GenerateResetPasswordLink {

    private final URL baseUrl;
    private final ProvideResetPasswordToken provideResetPasswordToken;

    @Override
    public LinkForPasswordReset execute(UserId userId) {

        final var token = provideResetPasswordToken.execute(userId);
        return new LinkForPasswordReset(
            baseUrl.toString() + "/reset-password?token=" + URLEncoder.encode(token.toString(), StandardCharsets.UTF_8)
        );
    }
}
