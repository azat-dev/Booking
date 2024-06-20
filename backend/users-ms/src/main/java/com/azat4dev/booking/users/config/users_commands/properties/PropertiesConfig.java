package com.azat4dev.booking.users.config.users_commands.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
    EmailVerificationProperties.class,
    UsersPhotoBucketConfigProperties.class,
    ResetPasswordConfigProperties.class,
    EmailServerConfigProperties.class,
    JwtConfigProperties.class
})
@Configuration
public class PropertiesConfig {
}
