package com.azat4dev.booking.users.config.users_commands.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.email-server")
@AllArgsConstructor
public class EmailServerConfigProperties {
    @NotBlank
    @NotNull
    private final String host;
    @Max(65535)
    @NotNull
    private final int port;
    @NotBlank
    @NotNull
    private final String protocol;
    @NotBlank
    @NotNull
    private final String username;
    @NotBlank
    @NotNull
    private final String password;
}
