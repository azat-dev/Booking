package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.RequiredArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public final class GenerateResetPasswordLinkImpl implements GenerateResetPasswordLink {

    private final BaseUrl baseUrl;
    private final ProvideResetPasswordToken provideResetPasswordToken;

    @Override
    public LinkForPasswordReset execute(UserId userId) {

        final var token = provideResetPasswordToken.execute(userId);
        return new LinkForPasswordReset(
            baseUrl.toString() + "/reset-password?token=" + URLEncoder.encode(token.toString(), StandardCharsets.UTF_8)
        );
    }
}
