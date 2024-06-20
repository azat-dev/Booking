package com.azat4dev.booking.users.config.users_commands.properties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app.email-verification")
@Data
public class EmailVerificationProperties {

    @NotNull
    private final Duration tokenExpiresIn;
    @NotBlank
    @NotNull
    private final String sendEmailFromName;
    @Email
    private final String sendEmailFromAddress;
    @NotNull
    private final URL baseUrlForVerificationLink;
}
