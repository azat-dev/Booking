package com.azat4dev.booking.users.users_commands.application.config.domain;

import com.azat4dev.booking.common.domain.annotations.CommandHandlerBean;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.SendVerificationEmailHandler;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByTokenImpl;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils.*;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Configuration
public class EmailVerificationConfig {

    @Bean
    BuildEmailVerificationLink buildVerificationLink(
        @Value("${app.verification_email.base_verification_link_url}")
        URL baseVerificationLinkUrl
    ) {
        return token -> {
            return baseVerificationLinkUrl + "?token=" + URLEncoder.encode(token.value(), StandardCharsets.UTF_8);
        };
    }

    @CommandHandlerBean
    SendVerificationEmailHandler sendVerificationEmailCommandHandler(
        BuildEmailVerificationLink buildEmailVerificationLink,
        @Value("${app.verification_email.outgoing.fromAddress}")
        String fromAddress,
        @Value("${app.verification_email.outgoing.fromName}")
        String fromName,
        EmailService emailService,
        ProvideEmailVerificationToken provideEmailVerificationToken,
        DomainEventsBus domainEventsBus
    ) throws EmailAddress.WrongFormatException {
        return new SendVerificationEmailHandler(
            buildEmailVerificationLink,
            EmailAddress.checkAndMakeFromString(fromAddress),
            fromName,
            emailService,
            provideEmailVerificationToken,
            domainEventsBus
        );
    }

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
    public VerifyEmailByToken verifyEmailByToken(
        GetInfoForEmailVerificationToken emailVerificationTokenInfoProvider,
        Users users,
        TimeProvider timeProvider
    ) {
        return new VerifyEmailByTokenImpl(
            emailVerificationTokenInfoProvider,
            users,
            timeProvider
        );
    }

    @Bean
    public GetInfoForEmailVerificationToken emailVerificationTokenInfoProvider(JwtDecoder jwtDecoder) {
        return new GetInfoForEmailVerificationTokenImpl(jwtDecoder);
    }
}
