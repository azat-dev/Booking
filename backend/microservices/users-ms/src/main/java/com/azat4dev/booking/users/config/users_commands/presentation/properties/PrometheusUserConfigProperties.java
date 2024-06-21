package com.azat4dev.booking.users.config.users_commands.presentation.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.users.prometheus")
public final class PrometheusUserConfigProperties {

    private String username;
    private String password;

    public void clearCredentials() {
        this.password = null;
        this.username = null;
    }
}
