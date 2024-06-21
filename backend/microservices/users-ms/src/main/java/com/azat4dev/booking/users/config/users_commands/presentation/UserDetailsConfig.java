package com.azat4dev.booking.users.config.users_commands.presentation;

import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.UserDetailsServiceFacade;
import com.azat4dev.booking.users.config.users_commands.presentation.properties.PrometheusUserConfigProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@AllArgsConstructor
@EnableConfigurationProperties(PrometheusUserConfigProperties.class)
@Configuration
public class UserDetailsConfig {

    private final PrometheusUserConfigProperties prometheusUserConfig;
    private final PasswordEncoder passwordEncoder;

    private InMemoryUserDetailsManager getSystemUsersManager() {

        final var prometheusUser = User.withUsername(prometheusUserConfig.getUsername())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .password(prometheusUserConfig.getPassword())
            .roles(Roles.PROMETHEUS_USER.name())
            .passwordEncoder(passwordEncoder::encode)
            .build();

        prometheusUserConfig.clearCredentials();

        return new InMemoryUserDetailsManager(
            prometheusUser
        );
    }

    @Bean
    public UserDetailsService customUserDetailsService(UsersRepository usersRepository) {

        final var systemUsers = getSystemUsersManager();
        final var appUsers = new CustomUserDetailsService(usersRepository);
        return new UserDetailsServiceFacade(systemUsers, appUsers);
    }
}
