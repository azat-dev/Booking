package com.azat4dev.demobooking.users.users_commands.application.config.data;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.EncodeJwt;
import com.azat4dev.demobooking.users.users_commands.data.services.GetInfoForEmailVerificationTokenImpl;
import com.azat4dev.demobooking.users.users_commands.data.services.ProvideEmailVerificationTokenImpl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.GetInfoForEmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.ProvideEmailVerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class EmailVerificationConfig {

    @Bean
    public ProvideEmailVerificationToken emailVerificationTokenProvider(
        @Value("${app.verification_email.token_expiration_in_ms}")
        long tokenExpirationInMs,
        EncodeJwt encodeJwt,
        TimeProvider timeProvider
    ) {
        return new ProvideEmailVerificationTokenImpl(
            tokenExpirationInMs,
            encodeJwt,
            timeProvider
        );
    }

    @Bean
    public GetInfoForEmailVerificationToken emailVerificationTokenInfoProvider(JwtDecoder jwtDecoder) {
        return new GetInfoForEmailVerificationTokenImpl(jwtDecoder);
    }
}
