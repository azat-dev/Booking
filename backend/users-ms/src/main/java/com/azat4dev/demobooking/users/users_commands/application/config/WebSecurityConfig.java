package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.users.users_commands.data.services.PasswordServiceImpl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.Password;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(c -> c.requestMatchers(
                    HttpMethod.POST,
                    "/api/public/**"
                )
                .permitAll()
                .requestMatchers("/api/with-auth/**")
                .authenticated())
            .csrf(c -> c.ignoringRequestMatchers("/api/public/**", "/api/with-auth/**"))
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder(PasswordService passwordService) {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return passwordService.encodePassword(
                    Password.makeFromString(rawPassword.toString())
                ).value();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return passwordService.matches(
                    Password.makeFromString(rawPassword.toString()),
                    new EncodedPassword(encodedPassword)
                );
            }
        };
    }

    @Bean
    PasswordService passwordService() {
        return new PasswordServiceImpl(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }
}
