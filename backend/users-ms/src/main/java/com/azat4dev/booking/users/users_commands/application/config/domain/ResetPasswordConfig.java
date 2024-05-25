package com.azat4dev.booking.users.users_commands.application.config.domain;

import com.azat4dev.booking.common.domain.annotations.CommandHandlerBean;
import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtDataDecoder;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.CompletePasswordResetHandler;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.SendResetPasswordEmail;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.SendResetPasswordEmailImpl;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils.*;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
@RequiredArgsConstructor
public class ResetPasswordConfig {

    private final EmailService emailService;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;

    @Bean
    public SendResetPasswordEmail sendResetPasswordEmail(BuildResetPasswordEmail buildResetPasswordEmail) {
        return new SendResetPasswordEmailImpl(
            usersRepository,
            buildResetPasswordEmail,
            emailService,
            bus
        );
    }

    @CommandHandlerBean
    public CompletePasswordResetHandler completePasswordResetHandler(
        ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId
    ) {
        return new CompletePasswordResetHandler(
            validateTokenForPasswordResetAndGetUserId,
            usersRepository,
            bus
        );
    }

    @Bean
    public BuildResetPasswordEmail buildResetPasswordEmail(
        @Value("${app.reset_password.outgoing_email.fromName}") String fromName,
        @Value("${app.reset_password.outgoing_email.fromAddress}") String fromAddress,
        @Value("${app.reset_password.outgoing_email.subject}") String subject,
        GenerateResetPasswordLink generateResetPasswordLink
    ) throws EmailAddress.WrongFormatException {
        return new BuildResetPasswordEmailImpl(
            fromName,
            EmailAddress.checkAndMakeFromString(fromAddress),
            subject,
            generateResetPasswordLink
        );
    }

    @Bean
    public GenerateResetPasswordLink generateResetPasswordLink(
        @Value("${app.reset_password.base_verification_link_url}")
        URL baseUrl,
        ProvideResetPasswordToken provideResetPasswordToken
    ) {
        return new GenerateResetPasswordLinkImpl(
            baseUrl,
            provideResetPasswordToken
        );
    }

    @Bean
    ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId(
        GetInfoForPasswordResetToken getTokenInfo,
        TimeProvider timeProvider
    ) {
        return new ValidateTokenForPasswordResetAndGetUserIdImpl(
            getTokenInfo,
            timeProvider
        );
    }

    @Bean
    GetInfoForPasswordResetToken getInfoForPasswordResetToken(JwtDataDecoder decoder) {
        return new GetInfoForPasswordResetTokenImpl(decoder);
    }
}
