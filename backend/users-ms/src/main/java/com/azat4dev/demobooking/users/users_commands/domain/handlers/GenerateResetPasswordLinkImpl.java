package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public final class GenerateResetPasswordLinkImpl implements GenerateResetPasswordLink {

    private final URL baseUrl;
    private final GenerateResetPasswordToken generateResetPasswordToken;

    @Override
    public ResetPasswordLink execute(UserId userId) {

        final var token = generateResetPasswordToken.execute(userId);
        return new ResetPasswordLink(
            baseUrl.toString() + "/reset-password?token=" + URLEncoder.encode(token.toString(), StandardCharsets.UTF_8)
        );
    }
}
