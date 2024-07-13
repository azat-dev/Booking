package com.azat4dev.booking.users.config.users_commands.domain;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.SendVerificationEmailHandler;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.VerifyEmailByToken;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.VerifyEmailByTokenImpl;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils.*;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.config.users_commands.properties.EmailVerificationProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Configuration
@AllArgsConstructor
public class EmailVerificationConfig {

    private final EmailVerificationProperties emailVerificationProperties;
    private final JwtDataEncoder jwtDataEncoder;
    private final JwtDecoder jwtDecoder;
    private final TimeProvider timeProvider;
    private final Users users;
    private final EmailService emailService;
    private final DomainEventsBus domainEventsBus;

    @Bean
    BuildEmailVerificationLink buildVerificationLink() {
        return token -> {
            return emailVerificationProperties.getBaseUrlForVerificationLink() + "?token=" + URLEncoder.encode(token.value(), StandardCharsets.UTF_8);
        };
    }

    @Bean
    SendVerificationEmailHandler sendVerificationEmailCommandHandler(
        BuildEmailVerificationLink buildEmailVerificationLink,
        ProvideEmailVerificationToken provideEmailVerificationToken
    ) throws EmailAddress.WrongFormatException {
        return new SendVerificationEmailHandler(
            buildEmailVerificationLink,
            EmailAddress.checkAndMakeFromString(emailVerificationProperties.getSendEmailFromAddress()),
            emailVerificationProperties.getSendEmailFromName(),
            emailService,
            provideEmailVerificationToken,
            domainEventsBus
        );
    }

    @Bean
    public ProvideEmailVerificationToken emailVerificationTokenProvider() {
        return new ProvideEmailVerificationTokenImpl(
            emailVerificationProperties.getTokenExpiresIn(),
            jwtDataEncoder,
            timeProvider
        );
    }

    @Bean
    public VerifyEmailByToken verifyEmailByToken(
        GetInfoForEmailVerificationToken emailVerificationTokenInfoProvider
    ) {
        return new VerifyEmailByTokenImpl(
            emailVerificationTokenInfoProvider,
            users,
            timeProvider
        );
    }

    @Bean
    public GetInfoForEmailVerificationToken emailVerificationTokenInfoProvider() {
        return new GetInfoForEmailVerificationTokenImpl(jwtDecoder);
    }
}
