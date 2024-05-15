package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.services.password.reset.ProvideResetPasswordToken;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public final class GenerateResetPasswordLinkImpl implements GenerateResetPasswordLink {

    private final URL baseUrl;
    private final ProvideResetPasswordToken provideResetPasswordToken;

    @Override
    public ResetPasswordLink execute(UserId userId) {

        final var token = provideResetPasswordToken.execute(userId);
        return new ResetPasswordLink(
            baseUrl.toString() + "/reset-password?token=" + URLEncoder.encode(token.toString(), StandardCharsets.UTF_8)
        );
    }
}
