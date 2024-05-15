package com.azat4dev.demobooking.users.users_commands.application.config.data;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.email.verification.GetInfoForEmailVerificationTokenImpl;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.email.verification.ProvideEmailVerificationTokenImpl;
import com.azat4dev.demobooking.users.users_commands.domain.services.email.GetInfoForEmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.services.email.ProvideEmailVerificationToken;
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
        JwtDataEncoder jwtDataEncoder,
        TimeProvider timeProvider
    ) {
        return new ProvideEmailVerificationTokenImpl(
            tokenExpirationInMs,
            jwtDataEncoder,
            timeProvider
        );
    }

    @Bean
    public GetInfoForEmailVerificationToken emailVerificationTokenInfoProvider(JwtDecoder jwtDecoder) {
        return new GetInfoForEmailVerificationTokenImpl(jwtDecoder);
    }
}
