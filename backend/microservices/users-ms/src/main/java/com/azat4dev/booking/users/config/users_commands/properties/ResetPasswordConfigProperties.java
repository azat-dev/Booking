package com.azat4dev.booking.users.config.users_commands.properties;

import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@Getter
@ConfigurationProperties(prefix = "app.reset-password")
public class ResetPasswordConfigProperties {

    private final Duration tokenExpiresIn;
    private final String sendEmailFromName;
    private final EmailAddress sendEmailFromAddress;
    private final String sendEmailSubject;
    private final BaseUrl baseUrlForLink;

    @ConstructorBinding
    public ResetPasswordConfigProperties(
        @NotNull
        Duration tokenExpiresIn,
        String sendEmailFromName,
        String sendEmailFromAddress,
        @NotNull
        @NotBlank
        String sendEmailSubject,
        String baseUrlForLink
    ) throws BaseUrl.Exception.WrongFormatException, EmailAddress.WrongFormatException {

        if (tokenExpiresIn.isNegative() || tokenExpiresIn.isZero()) {
            throw new RuntimeException("Token expiration time must be greater than 0 seconds.");
        }

        this.tokenExpiresIn = tokenExpiresIn;
        this.sendEmailFromName = sendEmailFromName;
        this.sendEmailFromAddress = EmailAddress.checkAndMakeFromString(sendEmailFromAddress);
        this.sendEmailSubject = sendEmailSubject;
        this.baseUrlForLink = BaseUrl.checkAndMakeFrom(baseUrlForLink);
    }
}
